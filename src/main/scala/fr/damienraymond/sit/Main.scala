package fr.damienraymond.sit

import fr.damienraymond.sit.infrastructure.commandline.CommandLineInterface
import zio.URIO
import zio.console._

object Main extends zio.App {

  override def run(args: List[String]) =
    program(args)
      .foldM(error =>
        putStrLn(error.toString) *> URIO(1)
        , _ =>
          URIO(0)
      )

  val program = (args: List[String]) =>
    putStrLn(s"Received argument $args") *>
      CommandLineInterface.run(List.empty)

}
