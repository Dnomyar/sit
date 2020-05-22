package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.model.branch.BranchName
import fr.damienraymond.sit.domain.model.commit.{AbstractCommit, CommitHash, CommitHistory}
import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.CommitRepository
import zio.{IO, Ref}

class InMemoryCommitRepository(ref: Ref[Map[CommitHash, AbstractCommit]])
  extends InMemoryRepository[CommitHash, AbstractCommit](ref)
    with CommitRepository {
  override def getCommitsHistory(name: BranchName): IO[repository.RepositoryError, CommitHistory] = ???
}


object InMemoryCommitRepository extends InMemoryRepositoryFactory[CommitHash, AbstractCommit, InMemoryCommitRepository] {
  override protected def instantiate: Ref[Map[CommitHash, AbstractCommit]] => InMemoryCommitRepository =
    new InMemoryCommitRepository(_)
}