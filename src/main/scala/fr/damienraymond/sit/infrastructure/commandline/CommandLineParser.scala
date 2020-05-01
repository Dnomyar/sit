package fr.damienraymond.sit.infrastructure.commandline

import scopt.OParser

object CommandLineParser {

  def parse(args: List[String]): Option[InboundCommandLineCommand] = {
    val builder = OParser.builder[Option[InboundCommandLineCommand]]

    import builder._

    val parser =
      OParser.sequence(
        programName("sit"),
        cmd("commit").action((_, _) => Some(InboundCommandLineCommand.Commit()))
      )

    OParser.parse(parser, args, None).flatten
  }

}
