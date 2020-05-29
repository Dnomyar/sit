package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.model.file.{FileUpdatedDate, FilePath}
import fr.damienraymond.sit.domain.repository.FileLastUpdateRepository
import zio.Ref

class InMemoryFileLastUpdateRepository(ref: Ref[Map[FilePath, FileUpdatedDate]])
  extends InMemoryRepository[FilePath, FileUpdatedDate](ref)
    with FileLastUpdateRepository


object InMemoryFileLastUpdateRepository extends InMemoryRepositoryFactory[FilePath, FileUpdatedDate, InMemoryFileLastUpdateRepository] {
  override protected def instantiate: Ref[Map[FilePath, FileUpdatedDate]] => InMemoryFileLastUpdateRepository =
    new InMemoryFileLastUpdateRepository(_)
}