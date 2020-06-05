package fr.damienraymond.sit

import fr.damienraymond.ddd.command.CommandBus
import fr.damienraymond.ddd.query.QueryBus
import fr.damienraymond.sit.domain.command.CommitCommand
import fr.damienraymond.sit.domain.model.file.FileStatus
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
    putStrLn(s"Received argument $args") >>>
      Instantiation.instantiate >>=
      dispatchCommand(args)

  val dispatchCommand = (args: List[String]) => (instantiated: Instantiated) => {
    import instantiated._

    args match {
      case "commit" :: Nil =>
        CommandBus.dispatch(CommitCommand()).unit

      case "status" :: Nil =>
        for {
          fileStatus <- QueryBus.dispatch(StatusQuery())
          _ <- putStrLn("File status")
          _ <- putStrLn(fileStatus.fileUpdated.toList.sortBy(_._1.path).map{
            case (file, FileStatus.StagedAdded) => s"${scala.Console.GREEN}A  ${scala.Console.RESET}${file.path} - ${FileStatus.StagedAdded}"
            case (file, FileStatus.StagedDeleted) => s"${scala.Console.GREEN}D  ${scala.Console.RESET}${file.path} - ${FileStatus.StagedDeleted}"
            case (file, FileStatus.StagedUpdated) => s"${scala.Console.GREEN}M  ${scala.Console.RESET}${file.path} - ${FileStatus.StagedUpdated}"
            case (file, FileStatus.NotStagedUpdated) => s"${scala.Console.RED} M ${scala.Console.RESET}${file.path} - ${FileStatus.NotStagedUpdated}"
            case (file, FileStatus.NotStagedDeleted) => s"${scala.Console.RED} D ${scala.Console.RESET}${file.path} - ${FileStatus.NotStagedDeleted}"
            case (file, FileStatus.Untracked) => s"${scala.Console.RED}?? ${scala.Console.RESET}${file.path} - ${FileStatus.Untracked}"
          }.mkString("\n"))
        } yield ()

      case unhandledCommand =>
        ZIO.fail(s"Command not handled $unhandledCommand")

    }
  }

}
