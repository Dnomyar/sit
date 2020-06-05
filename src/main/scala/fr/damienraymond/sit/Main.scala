package fr.damienraymond.sit

import fr.damienraymond.ddd.command.CommandBus
import fr.damienraymond.ddd.query.QueryBus
import fr.damienraymond.sit.domain.command.{AddCommand, CommitCommand}
import fr.damienraymond.sit.domain.model.file.{FilePath, FileStatus}
import fr.damienraymond.sit.domain.query.StatusQuery
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
    putStrLn(s"Received argument $args") *>
      repl(args)
//      Instantiation.instantiate >>= dispatchCommand(args)

  val repl = (args: List[String]) => args match {
    case "repl" :: _ =>
      val repl = (instantiated: Instantiated) => (for {
        _ <- putStr("> ")
        commandsRaw <- getStrLn
        commands = commandsRaw.split(" ").toList
        _ <- dispatchCommand(commands)(instantiated)
      } yield ()).forever

      Instantiation.instantiate >>= repl

    case commands =>
      Instantiation.instantiate >>= dispatchCommand(commands)
  }

  val dispatchCommand = (args: List[String]) => (instantiated: Instantiated) => {
    import instantiated._

    args match {
      case "add" :: files if files.nonEmpty =>
        CommandBus.dispatch(AddCommand(files.map(FilePath(_)))).unit

      case "commit" :: Nil =>
        CommandBus.dispatch(CommitCommand()).unit

      case "status" :: Nil =>
        for {
          fileStatus <- QueryBus.dispatch(StatusQuery())
          _ <- putStrLn("File status")
          _ <- putStrLn(fileStatus.fileUpdated.toList.sortBy(_._1.path).map{
            case (file, FileStatus.StagedAdded) => s"${scala.Console.GREEN}A  ${scala.Console.RESET}${file.path}"
            case (file, FileStatus.StagedDeleted) => s"${scala.Console.GREEN}D  ${scala.Console.RESET}${file.path}"
            case (file, FileStatus.StagedUpdated) => s"${scala.Console.GREEN}M  ${scala.Console.RESET}${file.path}"
            case (file, FileStatus.NotStagedUpdated) => s"${scala.Console.RED} M ${scala.Console.RESET}${file.path}"
            case (file, FileStatus.NotStagedDeleted) => s"${scala.Console.RED} D ${scala.Console.RESET}${file.path}"
            case (file, FileStatus.Untracked) => s"${scala.Console.RED}?? ${scala.Console.RESET}${file.path}"
          }.mkString("\n"))
        } yield ()

      case unhandledCommand =>
        ZIO.fail(s"Command not handled $unhandledCommand")

    }
  }

}
