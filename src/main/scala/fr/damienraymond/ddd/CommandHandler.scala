package fr.damienraymond.ddd

import zio.ZIO
import zio.console.Console

trait CommandHandler[C <: Command] {
  type Error = Any
  def handle(command: C): ZIO[Console, Exception, Set[Event]]
}
