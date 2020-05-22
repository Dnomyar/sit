package fr.damienraymond.ddd

import zio.ZIO
import zio.console._


object CommandBus {

  def dispatch[C <: Command](message: C)(implicit CM: CommandHandler[C]): ZIO[Console, Any, Set[Event]] =
    putStrLn("Dispatching command") *>
      CM.handle(message)

}