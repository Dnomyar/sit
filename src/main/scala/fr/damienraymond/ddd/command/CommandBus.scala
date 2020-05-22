package fr.damienraymond.ddd.command

import fr.damienraymond.ddd.Event
import zio.ZIO
import zio.console.{Console, putStrLn}

object CommandBus {

  def dispatch[C <: Command](message: C)(implicit CM: CommandHandler[C]): ZIO[Console, Any, Set[Event]] =
    putStrLn("[CommandBus] Dispatching command") *>
      CM.handle(message)
        .tap(events => putStrLn(s"[CommandBus] $events"))

}
