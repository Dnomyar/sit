package fr.damienraymond.sit.infrastructure.service.change

import java.time.ZonedDateTime

import fr.damienraymond.sit.domain.model.file.{FileUpdatedDate, FilePath}
import fr.damienraymond.sit.domain.service.change.GetFileLastUpdate
import zio.IO

class GetFileLastUpdateImplementation extends GetFileLastUpdate {

  override def getFileLastUpdate(filename: FilePath): IO[GetFileLastUpdate.GetFileLastUpdateError, Either[GetFileLastUpdate.FileNotFound.type, FileUpdatedDate]] =
    IO.succeed(Right(FileUpdatedDate(ZonedDateTime.now())))

}
