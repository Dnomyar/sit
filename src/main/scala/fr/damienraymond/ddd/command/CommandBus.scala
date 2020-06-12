package fr.damienraymond.ddd.command

import fr.damienraymond.ddd.Event
import zio.ZIO
import zio.console.Console


class CommandBus(middlewares: List[CommandMiddleware]) {

  def dispatch[C <: Command](message: C)(implicit CM: CommandHandler[C]): ZIO[Console, Any, Set[Event]] = {
    ZIO.foldRight(middlewares)(CM.handle(message): ZIO[Console, Any, Set[Event]]) {
      case (middleware, element) =>
        ZIO(middleware.dispatch(() => element)(message))
    }.flatten
  }
}
