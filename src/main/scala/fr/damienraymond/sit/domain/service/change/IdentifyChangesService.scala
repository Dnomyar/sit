package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.{File, FileChanged, Filename}
import zio.IO
import zio.stream._

object IdentifyChangesService {

  def identify(state: Set[File]): IO[Any, Set[FileChanged]] =
    Stream
      .fromEffect(getTrackedFiles)
      .flatMap(Stream.fromIterable(_))
      .filterM(identifyIfFileWasUpdated)
      .mapM(identifyChange)
      .run(Sink.collectAllToSet)

  def getTrackedFiles: IO[Any, Set[Filename]] = ???

  def identifyIfFileWasUpdated(filename: Filename): IO[Any, Boolean] = ???

  def identifyChange(filename: Filename): IO[Any, FileChanged] = ???

}
