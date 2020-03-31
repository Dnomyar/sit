package fr.damienraymond.sit.domain.repository

import fr.damienraymond.sit.domain.model.{AbstractCommit, BranchName, CommitHash, CommitHistory}
import zio.IO

trait CommitRepository extends Repository[CommitHash, AbstractCommit] {

  def getCommitsHistory(name: BranchName): IO[RepositoryError, CommitHistory]

}
