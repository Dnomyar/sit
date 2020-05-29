package fr.damienraymond.ddd.query

import zio.ZIO
import zio.console.Console

trait QueryHandler[Q <: Query] {
  type Error = Any
  def handle(command: Q): ZIO[Console, Exception, Q#RETURN_TYPE]
}
