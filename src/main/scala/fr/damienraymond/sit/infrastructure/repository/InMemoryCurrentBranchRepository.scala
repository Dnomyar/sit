package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.model.branch.Branch
import fr.damienraymond.sit.domain.repository.CurrentBranchRepository
import zio.Ref

class InMemoryCurrentBranchRepository(ref: Ref[Option[Branch]])
  extends InMemorySingletonRepository[Branch](ref)
    with CurrentBranchRepository


object InMemoryCurrentBranchRepository extends InMemorySingletonRepositoryFactory[Branch, InMemoryCurrentBranchRepository] {
  override def instantiate: Ref[Option[Branch]] => InMemoryCurrentBranchRepository =
    new InMemoryCurrentBranchRepository(_)
}