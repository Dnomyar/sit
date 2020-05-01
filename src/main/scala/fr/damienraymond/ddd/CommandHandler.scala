package fr.damienraymond.ddd

import zio.IO

trait CommandHandler[-C <: Command, +E] {
  def handle(command: C): IO[E, Set[Event]]
}
