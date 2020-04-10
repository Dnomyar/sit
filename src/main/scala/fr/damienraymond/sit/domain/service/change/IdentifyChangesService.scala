package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.FileChanged
import fr.damienraymond.sit.domain.model.file.{File, Filename, TrackedFiles}
import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.TrackedFilesRepository
import zio.{IO, ZIO}
import zio.stream._

class IdentifyChangesService(trackedFilesRepository: TrackedFilesRepository,
                             identifyUpdatedFile: IdentifyUpdatedFile) {

  def identify(state: Set[File]) =
    Stream
      .fromEffect(getTrackedFiles)
      .flatMap(trackedFiled => Stream.fromIterable(trackedFiled.files))
      .filterM(identifyUpdatedFile.identifyUpdatedFile)
      .mapM(identifyChange)
      .run(Sink.collectAllToSet[FileChanged])

  private def getTrackedFiles: ZIO[Any, repository.RepositoryError, TrackedFiles] =
    trackedFilesRepository.get.map(_.getOrElse(TrackedFiles.empty))

  private def identifyChange(filename: Filename): IO[Any, FileChanged] = ???

}
