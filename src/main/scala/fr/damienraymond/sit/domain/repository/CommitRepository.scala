package fr.damienraymond.sit.domain.repository

import fr.damienraymond.sit.domain.model.branch.Branch
import fr.damienraymond.sit.domain.model.commit.{AbstractCommit, CommitHash, CommitHistory}
import zio.IO

trait CommitRepository extends Repository[CommitHash, AbstractCommit] {

  def getCommitsHistory(branch: Branch): IO[RepositoryError, CommitHistory]

}
