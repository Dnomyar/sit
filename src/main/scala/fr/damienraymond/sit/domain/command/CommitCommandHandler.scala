package fr.damienraymond.sit.domain.command

import fr.damienraymond.ddd.CommandHandler
import fr.damienraymond.sit.domain.command.CommitCommandHandlerError.CurrentBranchNotFound
import fr.damienraymond.sit.domain.event.CommitCreated
import fr.damienraymond.sit.domain.model.branch.Branch
import fr.damienraymond.sit.domain.model.commit.{AbstractCommit, CommitHistory}
import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository}
import fr.damienraymond.sit.domain.service.change.IdentifyChangesService
import zio.console._
import zio.{IO, ZIO}

class CommitCommandHandler(currentBranchRepository: CurrentBranchRepository,
                           commitRepository: CommitRepository,
                           identifyChangesService: IdentifyChangesService) extends CommandHandler[CommitCommand] {


  override def handle(command: CommitCommand) =
      for {
        currentBranch <- getCurrentBranch
        _ <- putStrLn(s"currentBranch = $currentBranch")
        commitHistory <- commitRepository.getCommitsHistory(currentBranch)
        _ <- putStrLn(s"commitHistory = $commitHistory")
        files = CommitHistory.applyCommits(commitHistory)
        _ <- putStrLn(s"files = $files")
        filesChanges <- identifyChangesService.identify(files)
        _ <- putStrLn(s"filesChanges = $filesChanges")
        newCommit = commitHistory.newCommit(filesChanges)
        _ <- putStrLn(s"newCommit = $newCommit")
        _ <- saveCommit(newCommit)
      } yield Set(CommitCreated(newCommit))

  val getCurrentBranch: IO[Exception, Branch] =
    currentBranchRepository
      .get
      .flatMap {
        case None => ZIO.fail(CurrentBranchNotFound())
        case Some(branchName) => ZIO.succeed(branchName)
      }


  def saveCommit(commit: AbstractCommit): IO[repository.RepositoryError, Unit] =
    commitRepository.save(commit.hash, commit)

}

sealed trait CommitCommandHandlerError extends Exception

object CommitCommandHandlerError {

  case class CurrentBranchNotFound() extends CommitCommandHandlerError

}

