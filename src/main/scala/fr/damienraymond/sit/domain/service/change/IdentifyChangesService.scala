package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.FileChanged
import fr.damienraymond.sit.domain.model.file.{File, Filename, TrackedFiles}
import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.TrackedFilesRepository
import zio.ZIO
import zio.stream._

class IdentifyChangesService(trackedFilesRepository: TrackedFilesRepository,
                             identifyUpdatedFile: IdentifyUpdatedFile,
                             readFileService: ReadFileService,
                             diffService: DiffService) {

  def identify(state: Set[File]): ZIO[Any, Exception, Set[FileChanged]] =
    Stream
      .fromEffect(getTrackedFiles)
      .flatMap(trackedFiled => Stream.fromIterable(trackedFiled.files))
      .filterM(identifyUpdatedFile.identifyUpdatedFile)
      .mapM(identifyChange(state))
      .run(Sink.collectAllToSet[FileChanged])

  private def getTrackedFiles: ZIO[Any, repository.RepositoryError, TrackedFiles] =
    trackedFilesRepository.get.map(_.getOrElse(TrackedFiles.empty))

  private def identifyChange(state: Set[File])(filename: Filename): ZIO[Any, Exception, FileChanged] =
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
      .map(FileChanged(filename, _))


}
