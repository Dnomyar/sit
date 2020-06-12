package fr.damienraymond.sit.domain.command

import fr.damienraymond.ddd.command.CommandHandler
import fr.damienraymond.sit.domain.event.CommitCreated
import fr.damienraymond.sit.domain.model.commit.{AbstractCommit, CommitHistory}
import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository}
import fr.damienraymond.sit.domain.service.change.{CurrentBranchService, IdentifyChangesService}
import zio.ZIO
import zio.console._

class CommitCommandHandler(currentBranchService: CurrentBranchService,
                           currentBranchRepository: CurrentBranchRepository,
                           commitRepository: CommitRepository,
                           identifyChangesService: IdentifyChangesService) extends CommandHandler[CommitCommand] {


  override def handle(command: CommitCommand) =
      for {
        currentBranch <- currentBranchService.getCurrentBranch
        _ <- putStrLn(s"[CommitCommandHandler] currentBranch = $currentBranch")
        commitHistory <- commitRepository.getCommitsHistory(currentBranch)
        _ <- putStrLn(s"[CommitCommandHandler] commitHistory = $commitHistory")
        files = CommitHistory.applyCommits(commitHistory)
        _ <- putStrLn(s"[CommitCommandHandler] files = $files")
        filesChanges <- identifyChangesService.identifyChanges(files)
        _ <- putStrLn(s"[CommitCommandHandler] filesChanges = $filesChanges")
        newCommit = commitHistory.newCommit(filesChanges)
        _ <- saveCommit(newCommit)
        _ <- currentBranchRepository.save(currentBranch.withNewHead(newCommit.hash))
      } yield Set(CommitCreated(newCommit))


  def saveCommit(commit: AbstractCommit): ZIO[Console, repository.RepositoryError, Unit] =
    putStrLn(s"[CommitCommandHandler] commit create = ${commit.hash}") *>
      commitRepository.save(commit.hash, commit)

}


