package fr.damienraymond.ddd.query

import zio.ZIO
import zio.console._

object QueryBus {

  def dispatch[C <: Query](message: C)(implicit CM: QueryHandler[C]): ZIO[Console, Any, C#RETURN_TYPE] =
    putStrLn("[QueryBus] Dispatching query") *>
      CM.handle(message)

}
