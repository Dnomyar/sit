package fr.damienraymond.ddd.query

import zio.ZIO
import zio.console._

object QueryBus {

  def dispatch[T, C <: Query[T]](message: C)(implicit CM: QueryHandler[T, C]): ZIO[Console, Any, T] =
    putStrLn("[QueryBus] Dispatching command") *>
      CM.handle(message)

}
