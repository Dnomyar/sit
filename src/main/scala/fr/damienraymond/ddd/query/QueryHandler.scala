package fr.damienraymond.ddd.query

import zio.ZIO
import zio.console.Console

trait QueryHandler[T, Q <: Query[T]] {
  type Error = Any
  def handle(command: Q): ZIO[Console, Exception, T]
}
