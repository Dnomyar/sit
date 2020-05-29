package fr.damienraymond.sit.domain.query

import fr.damienraymond.ddd.query.QueryHandler
import fr.damienraymond.sit.domain.query.model.StatusQueryResponse
import fr.damienraymond.sit.domain.service.change.IdentifyChangesService
import zio.ZIO
import zio.console.Console

class StatusQueryHandler(identifyChangesService: IdentifyChangesService) extends QueryHandler[StatusQuery]{
  override def handle(command: StatusQuery): ZIO[Console, Exception, StatusQueryResponse] = {
    for {
      filesStatus <- identifyChangesService.identifyUpdatedFiles
    } yield StatusQueryResponse(filesStatus)
  }
}
