package fr.damienraymond.sit

import fr.damienraymond.ddd.command.CommandBus
import fr.damienraymond.sit.domain.command.CommitCommand
import zio.console._
import zio.{URIO, ZIO}

object Main extends zio.App {

  override def run(args: List[String]) =
    program(args)
      .foldM(error =>
        putStrLn(s"Error: ${error.toString}") *> URIO(1)
        , _ =>
          URIO(0)
      )

  val program = (args: List[String]) =>
    putStrLn(s"Received argument $args") >>>
      Instantiation.instantiate >>=
      dispatchCommand(args)

  val dispatchCommand = (args: List[String]) => (instantiated: Instantiated) => {
    import instantiated._

    args match {
      case "commit" :: Nil =>
        CommandBus.dispatch(CommitCommand()).unit

      case unhandledCommand =>
        ZIO.fail(s"Command not handled $unhandledCommand")

    }
  }

}
