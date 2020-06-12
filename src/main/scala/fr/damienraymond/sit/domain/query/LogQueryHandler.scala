package fr.damienraymond.sit.domain.query

import fr.damienraymond.ddd.query.QueryHandler
import fr.damienraymond.sit.domain.query.model.LogQueryResponse
import fr.damienraymond.sit.domain.repository.CommitRepository
import fr.damienraymond.sit.domain.service.change.CurrentBranchService
import zio.ZIO
import zio.console.Console

class LogQueryHandler(currentBranchService: CurrentBranchService,
                      commitRepository: CommitRepository) extends QueryHandler[LogQuery] {
  override def handle(command: LogQuery): ZIO[Console, Exception, LogQueryResponse] = {

    for {
      currentBranch <- currentBranchService.getCurrentBranch
      commitHistory <- commitRepository.getCommitsHistory(currentBranch)
    } yield LogQueryResponse(commitHistory.commits.map(commit => (commit.hash.hash, commit.changes.toString())))

  }

}
