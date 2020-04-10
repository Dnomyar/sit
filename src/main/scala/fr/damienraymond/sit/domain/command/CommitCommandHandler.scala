package fr.damienraymond.sit.domain.command

import fr.damienraymond.sit.domain.command.CommitCommandHandlerError.CurrentBranchNotFound
import fr.damienraymond.sit.domain.event.CommitCreated
import fr.damienraymond.sit.domain.model.branch.Branch
import fr.damienraymond.sit.domain.model.commit.{AbstractCommit, CommitHistory}
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository}
import fr.damienraymond.sit.domain.service.change.IdentifyChangesService
import fr.damienraymond.sit.domain.{event, repository}
import zio.ZIO

class CommitCommandHandler(currentBranchRepository: CurrentBranchRepository,
                           commitRepository: CommitRepository,
                           identifyChangesService: IdentifyChangesService) extends CommandHandler[CommitRepository with CurrentBranchRepository, CommitCommand, Any] {

  override def handle(command: CommitCommand): ZIO[CommitRepository with CurrentBranchRepository, Any, Set[event.Event]] =
    for {
      currentBranch <- getCurrentBranch
      commitHistory <- commitRepository.getCommitsHistory(currentBranch.name)
      files = CommitHistory.applyCommits(commitHistory)
      filesChanges <- identifyChangesService.identify(files)
      newCommit = commitHistory.newCommit(filesChanges)
      _ <- saveCommit(newCommit)
    } yield Set(CommitCreated(newCommit))


  val getCurrentBranch: ZIO[CurrentBranchRepository, Object, Branch] =
    currentBranchRepository
      .get
      .flatMap {
        case None => ZIO.fail(CurrentBranchNotFound())
        case Some(branchName) => ZIO.succeed(branchName)
      }


  def saveCommit(commit: AbstractCommit): ZIO[CommitRepository, repository.RepositoryError, Unit] =
    commitRepository.save(commit.hash, commit)

}

sealed trait CommitCommandHandlerError

object CommitCommandHandlerError {

  case class CurrentBranchNotFound() extends CommitCommandHandlerError

}

