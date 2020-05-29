package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.{FilePath, FileUpdatedDate}
import fr.damienraymond.sit.domain.service.change.GetFileLastUpdate.FileNotFound
import zio.IO

trait GetFileLastUpdate {

  def getFileLastUpdate(filename: FilePath): IO[GetFileLastUpdate.GetFileLastUpdateError, Either[FileNotFound.type, FileUpdatedDate]]

}

object GetFileLastUpdate {

  sealed trait GetFileLastUpdateError extends Exception

  case object FileNotFound

}
