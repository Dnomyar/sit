package fr.damienraymond.sit.domain.query

import fr.damienraymond.ddd.query.QueryHandler
import fr.damienraymond.sit.domain.query.model.StatusQueryResponse
import zio.ZIO
import zio.console.Console

class StatusQueryHandler extends QueryHandler[StatusQueryResponse, StatusQuery]{
  override def handle(command: StatusQuery): ZIO[Console, Exception, StatusQueryResponse] = {
    ???
  }
}
