package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.FileChanged
import fr.damienraymond.sit.domain.model.file.{File, Filename}
import zio.IO
import zio.stream._

class IdentifyChangesService(identifyUpdatedFile: IdentifyUpdatedFile) {

  def identify(state: Set[File]) =
    Stream
      .fromEffect(getTrackedFiles)
      .flatMap(Stream.fromIterable(_))
      .filterM(identifyUpdatedFile.identifyUpdatedFile)
      .mapM(identifyChange)
      .run(Sink.collectAllToSet[FileChanged])

  private def getTrackedFiles: IO[Any, Set[Filename]] = ???

  private def identifyChange(filename: Filename): IO[Any, FileChanged] = ???

}
