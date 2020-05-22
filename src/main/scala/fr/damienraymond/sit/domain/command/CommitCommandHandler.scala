package fr.damienraymond.sit.domain.command

import fr.damienraymond.ddd.{CommandHandler, Event}
import fr.damienraymond.sit.domain.command.CommitCommandHandlerError.CurrentBranchNotFound
import fr.damienraymond.sit.domain.event.CommitCreated
import fr.damienraymond.sit.domain.model.branch.Branch
import fr.damienraymond.sit.domain.model.commit.{AbstractCommit, CommitHistory}
import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository}
import fr.damienraymond.sit.domain.service.change.IdentifyChangesService
import zio.{IO, ZIO}

class CommitCommandHandler(currentBranchRepository: CurrentBranchRepository,
                           commitRepository: CommitRepository,
                           identifyChangesService: IdentifyChangesService) extends CommandHandler[CommitCommand] {


  override def handle(command: CommitCommand): IO[Any, Set[Event]] =
      for {
        currentBranch <- getCurrentBranch
        commitHistory <- commitRepository.getCommitsHistory(currentBranch.name)
        files = CommitHistory.applyCommits(commitHistory)
        filesChanges <- identifyChangesService.identify(files)
        newCommit = commitHistory.newCommit(filesChanges)
        _ <- saveCommit(newCommit)
      } yield Set(CommitCreated(newCommit))

  val getCurrentBranch: IO[Object, Branch] =
    currentBranchRepository
      .get
      .flatMap {
        case None => ZIO.fail(CurrentBranchNotFound())
        case Some(branchName) => ZIO.succeed(branchName)
      }


  def saveCommit(commit: AbstractCommit): IO[repository.RepositoryError, Unit] =
    commitRepository.save(commit.hash, commit)

}

sealed trait CommitCommandHandlerError

object CommitCommandHandlerError {

  case class CurrentBranchNotFound() extends CommitCommandHandlerError

}

