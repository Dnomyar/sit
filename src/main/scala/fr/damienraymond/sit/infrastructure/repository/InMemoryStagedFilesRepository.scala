package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.model.file.StagedFiles
import fr.damienraymond.sit.domain.repository.StagedFilesRepository
import zio.Ref

class InMemoryStagedFilesRepository(ref: Ref[Option[StagedFiles]])
  extends InMemorySingletonRepository[StagedFiles](ref)
    with StagedFilesRepository

object InMemoryStagedFilesRepository extends InMemorySingletonRepositoryFactory[StagedFiles, InMemoryStagedFilesRepository] {
  override protected def instantiate: Ref[Option[StagedFiles]] => InMemoryStagedFilesRepository =
    new InMemoryStagedFilesRepository(_)
}