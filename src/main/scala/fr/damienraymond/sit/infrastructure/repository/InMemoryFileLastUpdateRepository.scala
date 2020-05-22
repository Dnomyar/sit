package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.model.file.{FileUpdatedDate, Filename}
import fr.damienraymond.sit.domain.repository.FileLastUpdateRepository
import zio.Ref

class InMemoryFileLastUpdateRepository(ref: Ref[Map[Filename, FileUpdatedDate]])
  extends InMemoryRepository[Filename, FileUpdatedDate](ref)
    with FileLastUpdateRepository


object InMemoryFileLastUpdateRepository extends InMemoryRepositoryFactory[Filename, FileUpdatedDate, InMemoryFileLastUpdateRepository] {
  override protected def instantiate: Ref[Map[Filename, FileUpdatedDate]] => InMemoryFileLastUpdateRepository =
    new InMemoryFileLastUpdateRepository(_)
}