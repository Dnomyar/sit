package fr.damienraymond.sit.domain

import fr.damienraymond.ddd.query.Query
import fr.damienraymond.sit.domain.query.model.{LogQueryResponse, StatusQueryResponse}

package object query {

  case class StatusQuery() extends Query {
    override type RETURN_TYPE = StatusQueryResponse
  }

  case class LogQuery() extends Query {
    override type RETURN_TYPE = LogQueryResponse
  }

}
