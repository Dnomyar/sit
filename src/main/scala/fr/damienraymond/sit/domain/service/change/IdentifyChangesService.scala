package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file
import fr.damienraymond.sit.domain.model.file.{File, FileChanged, FileChangedStatus, FilePath, FileStatus, StagedFiles, TrackedFiles}
import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.{StagedFilesRepository, TrackedFilesRepository}
import zio.ZIO
import zio.stream._

class IdentifyChangesService(stagedFileRepository: StagedFilesRepository,
                             trackedFilesRepository: TrackedFilesRepository,
                             identifyUpdatedFile: IdentifyUpdatedFile,
                             readFileService: ReadFileService,
                             fileSystemFiles: FileSystemFiles,
                             diffService: DiffService) {


  def identifyUpdatedFiles: ZIO[Any, Exception, Map[FilePath, FileStatus]] = {
    (trackedFilesRepository.get <*> stagedFileRepository.get <*> fileSystemFiles.allFiles).flatMap {
      case ((maybeTrackedFiles, maybeStagedFiles), scannedFiles) =>
        val trackedFiles = maybeTrackedFiles.getOrElse(TrackedFiles.empty)
        val stagedFiles = maybeStagedFiles.getOrElse(StagedFiles.empty)

        val notTrackedFiles = scannedFiles.diff(trackedFiles.files)

        val allFiles = trackedFiles.files ++ scannedFiles

        for {
          allFilesStatus <- ZIO.foreachPar(allFiles) {
            file =>
              for {
                fileStatus <- identifyUpdatedFile.identifyFileStatus(file)
              } yield (file, fileStatus)
          }

          deletedFiles = allFilesStatus.collect { case (file, FileChangedStatus.Deleted) => file }.toSet
          addedFiles = allFilesStatus.collect { case (file, FileChangedStatus.Added) => file }.toSet
          updatedFiles = allFilesStatus.collect { case (file, FileChangedStatus.Updated) => file }.toSet

          notStagedFiles = (allFiles diff stagedFiles.files) diff notTrackedFiles

          stagedAdded = stagedFiles.files intersect addedFiles
          stagedDeleted = stagedFiles.files intersect deletedFiles
          stagedUpdated = stagedFiles.files intersect updatedFiles

          notStagedDeleted = notStagedFiles intersect deletedFiles
          notStagedUpdated = notStagedFiles intersect (updatedFiles union addedFiles)

        } yield (
          notTrackedFiles.map(file => (file, FileStatus.Untracked)).toMap ++
            stagedAdded.map(file => (file, FileStatus.StagedAdded)).toMap ++
            stagedDeleted.map(file => (file, FileStatus.StagedDeleted)).toMap ++
            stagedUpdated.map(file => (file, FileStatus.StagedUpdated)).toMap ++
            notStagedUpdated.map(file => (file, FileStatus.NotStagedUpdated)).toMap ++
            notStagedDeleted.map(file => (file, FileStatus.NotStagedDeleted)).toMap
          )
    }
  }

  def identifyChanges(state: Set[File]): ZIO[Any, Exception, Set[FileChanged]] =
    Stream
      .fromEffect(getStagedFiles)
      .flatMap(trackedFiled => Stream.fromIterable(trackedFiled.files))
      .filterM(identifyUpdatedFile.identifyUpdatedFile)
      .mapM(identifyChange(state))
      .run(Sink.collectAllToSet[FileChanged])

  private def getStagedFiles: ZIO[Any, repository.RepositoryError, StagedFiles] =
    stagedFileRepository.get.map(_.getOrElse(StagedFiles.empty))

  private def identifyChange(state: Set[File])(filename: FilePath): ZIO[Any, Exception, FileChanged] =
    readFileService
      .readFile(filename)
      .map { newVersionFileContent =>
        val lastKnownFileContent =
          state
            .find(_.filename == filename)
            .map(_.content.values)
            .getOrElse(Iterable.empty)

        diffService.identifyChanges(lastKnownFileContent, newVersionFileContent)
      }
      .map(file.FileChanged(filename, _))


}
