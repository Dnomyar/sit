package fr.damienraymond.sit.domain.repository

import fr.damienraymond.sit.domain.model.branch.BranchName
import fr.damienraymond.sit.domain.model.commit.{AbstractCommit, CommitHash, CommitHistory}
import zio.IO

trait CommitRepository extends Repository[CommitHash, AbstractCommit] {

  def getCommitsHistory(name: BranchName): IO[RepositoryError, CommitHistory]

}
