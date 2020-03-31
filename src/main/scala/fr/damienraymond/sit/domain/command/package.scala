package fr.damienraymond.sit.domain

import fr.damienraymond.sit.domain.event.Event
import zio.ZIO

package object command {

  sealed trait Command {
    def handler: CommandHandler[_, _, _]
  }

  case class CommitCommand() extends Command {
    override val handler = CommitCommandHandler
  }


  trait CommandHandler[ENV, C <: Command, E] {
    def handle(command: C): ZIO[ENV, E, Set[Event]]
  }

}
