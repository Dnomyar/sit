package fr.damienraymond.sit.domain.query

import fr.damienraymond.ddd.query.QueryHandler
import fr.damienraymond.sit.domain.query.model.StatusQueryResponse
import fr.damienraymond.sit.domain.service.change.{IdentifyChangesService, IgnoreService}
import zio.ZIO
import zio.console.Console

class StatusQueryHandler(identifyChangesService: IdentifyChangesService,
                         ignoreService: IgnoreService) extends QueryHandler[StatusQuery]{
  override def handle(command: StatusQuery): ZIO[Console, Exception, StatusQueryResponse] = {
    for {
      filesStatus <- identifyChangesService.identifyUpdatedFiles
      filesWithoutIgnoredStatus = filesStatus.filter{
        case (path, _) => ignoreService.shouldKeepFile(path)
      }
    } yield StatusQueryResponse(filesWithoutIgnoredStatus)
  }
}
