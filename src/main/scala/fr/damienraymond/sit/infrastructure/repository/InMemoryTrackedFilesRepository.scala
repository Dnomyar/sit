package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.model.file.TrackedFiles
import fr.damienraymond.sit.domain.repository.TrackedFilesRepository
import zio.Ref

class InMemoryTrackedFilesRepository(ref: Ref[Option[TrackedFiles]])
  extends InMemorySingletonRepository[TrackedFiles](ref)
    with TrackedFilesRepository


object InMemoryTrackedFilesRepository extends InMemorySingletonRepositoryFactory[TrackedFiles, InMemoryTrackedFilesRepository] {
  override protected def instantiate: Ref[Option[TrackedFiles]] => InMemoryTrackedFilesRepository =
    new InMemoryTrackedFilesRepository(_)
}