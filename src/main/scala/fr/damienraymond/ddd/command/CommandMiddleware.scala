package fr.damienraymond.ddd.command

import fr.damienraymond.ddd.Event
import zio.ZIO
import zio.console.Console

trait CommandMiddleware {

  def dispatch(next: () => ZIO[Console, Any, Set[Event]])(command: Command): ZIO[Console, Any, Set[Event]]

}
