package fr.damienraymond.sit.infrastructure.service.change

import java.time.ZonedDateTime

import fr.damienraymond.sit.domain.model.file.{FileUpdatedDate, Filename}
import fr.damienraymond.sit.domain.service.change.GetFileLastUpdate
import zio.IO

class GetFileLastUpdateImplementation extends GetFileLastUpdate {
  override def getFileLastUpdate(filename: Filename): IO[GetFileLastUpdate.GetFileLastUpdateError, FileUpdatedDate] =
    IO.succeed(FileUpdatedDate(ZonedDateTime.now()))
}
