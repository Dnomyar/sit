package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.{FileUpdatedDate, Filename}
import zio.IO

trait GetFileLastUpdate {

  def getFileLastUpdate(filename: Filename): IO[GetFileLastUpdate.GetFileLastUpdateError, FileUpdatedDate]

}

object GetFileLastUpdate {

  sealed trait GetFileLastUpdateError extends Exception

}
