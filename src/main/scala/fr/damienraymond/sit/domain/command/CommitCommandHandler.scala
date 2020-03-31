package fr.damienraymond.sit.domain.command

import fr.damienraymond.sit.domain.command.CommitCommandHandlerError.CurrentBranchNotFound
import fr.damienraymond.sit.domain.event.CommitCreated
import fr.damienraymond.sit.domain.model.{AbstractCommit, Branch, BranchName, CommitHistory}
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository}
import fr.damienraymond.sit.domain.service.change.IdentifyChangesService
import fr.damienraymond.sit.domain.{event, repository}
import zio.ZIO

object CommitCommandHandler extends CommandHandler[CommitRepository with CurrentBranchRepository, CommitCommand, Any] {

  override def handle(command: CommitCommand): ZIO[CommitRepository with CurrentBranchRepository, Any, Set[event.Event]] =
    for {
      currentBranch <- getCurrentBranch
      commitHistory <- getCommitHistory(currentBranch.name)
      files = AbstractCommit.applyCommits(commitHistory)
      filesChanges <- IdentifyChangesService.identify(files)
      newCommit = commitHistory.newCommit(filesChanges)
      _ <- saveCommit(newCommit)
    } yield Set(CommitCreated(newCommit))



  val getCurrentBranch: ZIO[CurrentBranchRepository, Object, Branch] =
    ZIO
      .accessM[CurrentBranchRepository](_.get)
      .flatMap {
        case None => ZIO.fail(CurrentBranchNotFound())
        case Some(branchName) => ZIO.succeed(branchName)
      }

  def getCommitHistory(branchName: BranchName): ZIO[CommitRepository, repository.RepositoryError, CommitHistory] =
    ZIO.accessM[CommitRepository](_.getCommitsHistory(branchName))


  def saveCommit(commit: AbstractCommit): ZIO[CommitRepository, repository.RepositoryError, Unit] =
    ZIO.accessM[CommitRepository](_.save(commit.hash, commit))

}

sealed trait CommitCommandHandlerError

object CommitCommandHandlerError {

  case class CurrentBranchNotFound() extends CommitCommandHandlerError

}

