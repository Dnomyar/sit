package fr.damienraymond.sit.infrastructure.commandline

sealed trait InboundCommandLineCommand

object InboundCommandLineCommand {
  case class Commit() extends InboundCommandLineCommand
  case class Status() extends InboundCommandLineCommand
  case class Diff() extends InboundCommandLineCommand
  case class Log() extends InboundCommandLineCommand
}
