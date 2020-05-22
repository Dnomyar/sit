package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.{FileUpdatedDate, Filename}
import fr.damienraymond.sit.domain.repository.FileLastUpdateRepository
import zio.ZIO

class IdentifyUpdatedFile(fileLastUpdateRepository: FileLastUpdateRepository, getFileLastUpdate: GetFileLastUpdate) {

  def identifyUpdatedFile(filename: Filename): ZIO[Any, Exception, Boolean] = {
    for {
      lastKnownUpdate <- getLastKnownUpdate(filename)
      actualLastFile <- getFileLastUpdate.getFileLastUpdate(filename)
    } yield lastKnownUpdate != actualLastFile
  }

  private def getLastKnownUpdate(filename: Filename): ZIO[Any, Exception, FileUpdatedDate] =
    fileLastUpdateRepository
      .get(filename)
      .flatMap {
        case None => ZIO.fail(IdentifyUpdatedFile.LastKnownUpdateNotFound())
        case Some(lastKnownUpdate) => ZIO.succeed(lastKnownUpdate)
      }


}

object IdentifyUpdatedFile {

  sealed trait IdentifyUpdatedFileError extends Exception

  case class LastKnownUpdateNotFound() extends IdentifyUpdatedFileError

}
