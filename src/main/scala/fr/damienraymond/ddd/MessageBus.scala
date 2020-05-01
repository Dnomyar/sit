package fr.damienraymond.ddd

import zio.ZIO
import zio.console._

trait MessageBus[MESSAGE_TYPE <: Event] {

  def dispatch[M <: MESSAGE_TYPE](message: M): ZIO[Console, _, Set[Event]]

}


object CommandBus extends MessageBus[Command] {

  override def dispatch[M <: Command](message: M): ZIO[Console, _, Set[Event]] =
    putStrLn("Dispatching command") *>
      message.handler.handle(message)

}