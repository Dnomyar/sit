package fr.damienraymond.ddd

import zio.IO

trait CommandHandler[C <: Command] {
  type Error = Any
  def handle(command: C): IO[Error, Set[Event]]
}
