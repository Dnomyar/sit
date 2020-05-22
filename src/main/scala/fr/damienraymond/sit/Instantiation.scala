package fr.damienraymond.sit

import fr.damienraymond.sit.domain.command.CommitCommandHandler
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository, FileLastUpdateRepository, TrackedFilesRepository}
import fr.damienraymond.sit.domain.service.change.{IdentifyUpdatedFile, _}
import fr.damienraymond.sit.infrastructure.repository.{InMemoryCommitRepository, InMemoryCurrentBranchRepository, InMemoryFileLastUpdateRepository, InMemoryTrackedFilesRepository}
import fr.damienraymond.sit.infrastructure.service.change.{GetFileLastUpdateImplementation, ReadFileServiceImplementation}
import zio.ZIO


trait Instantiated {
  implicit val commitCommandHandler: CommitCommandHandler
}




object Instantiation {

  val instantiate: ZIO[Any, NoSuchElementException, Instantiated] = {
    for {

      currentBranchRepository: CurrentBranchRepository <- InMemoryCurrentBranchRepository.create
      commitRepository: CommitRepository <- InMemoryCommitRepository.create
      trackedFilesRepository: TrackedFilesRepository <- InMemoryTrackedFilesRepository.create // InMemorySingletonRepository.empty[TrackedFiles]

      fileLastUpdateRepository: FileLastUpdateRepository <- InMemoryFileLastUpdateRepository.create // InMemoryRepository.empty[Filename, FileUpdatedDate]

      getFileLastUpdate: GetFileLastUpdate = new GetFileLastUpdateImplementation
      readFileService: ReadFileService = ReadFileServiceImplementation
      diffService: DiffService = DummyDiffService

      identifyUpdatedFile: IdentifyUpdatedFile = new IdentifyUpdatedFile(fileLastUpdateRepository, getFileLastUpdate)
      identifyChangesService: IdentifyChangesService = new IdentifyChangesService(
        trackedFilesRepository,
        identifyUpdatedFile,
        readFileService,
        diffService
      )


    } yield new Instantiated {
      override implicit val commitCommandHandler: CommitCommandHandler = new CommitCommandHandler(
        currentBranchRepository,
        commitRepository,
        identifyChangesService
      )
    }
  }

}
