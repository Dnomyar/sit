package fr.damienraymond.sit.infrastructure.commandline

import fr.damienraymond.ddd.CommandBus
import fr.damienraymond.sit.domain.command.CommitCommand
import zio.ZIO


object CommandLineInterface {


  def run(args: List[String]) = {
    CommandLineParser.parse(args) match {
      case Some(InboundCommandLineCommand.Commit()) =>
        CommandBus
          .dispatch(CommitCommand())
          .flatMap { events =>
            ZIO.unit
          }
      case _ =>
        ZIO.fail("Command not handled")
    }
  }


}
