package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.model.branch.Branch
import fr.damienraymond.sit.domain.model.commit.{AbstractCommit, CommitHash, CommitHistory, OrphanCommit}
import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.CommitRepository
import zio.{IO, Ref}

class InMemoryCommitRepository(ref: Ref[Map[CommitHash, AbstractCommit]])
  extends InMemoryRepository[CommitHash, AbstractCommit](ref)
    with CommitRepository {
  override def getCommitsHistory(branch: Branch): IO[repository.RepositoryError, CommitHistory] =
    ref.get.map { repo =>

      def loop(commitHash: CommitHash, allCommits: Map[CommitHash, AbstractCommit], commitsAcc: List[AbstractCommit]): List[AbstractCommit] = {
        allCommits.get(commitHash) match {
          case Some(commit: OrphanCommit) => commit :: commitsAcc
          case Some(commit: AbstractCommit) => loop(commit.hash, allCommits - commit.hash, commit :: commitsAcc)
          case _ => commitsAcc
        }
      }

      CommitHistory(loop(branch.head, repo, List.empty).reverse)

    }
}


object InMemoryCommitRepository extends InMemoryRepositoryFactory[CommitHash, AbstractCommit, InMemoryCommitRepository] {
  override protected def instantiate: Ref[Map[CommitHash, AbstractCommit]] => InMemoryCommitRepository =
    new InMemoryCommitRepository(_)
}