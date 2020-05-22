package fr.damienraymond.sit

import java.time.ZonedDateTime

import fr.damienraymond.sit.domain.command.CommitCommandHandler
import fr.damienraymond.sit.domain.model.branch.BranchName
import fr.damienraymond.sit.domain.model.change.LinesAdded
import fr.damienraymond.sit.domain.model.commit.{CommitHash, OrphanCommit}
import fr.damienraymond.sit.domain.model.file.{FileUpdatedDate, Filename, TrackedFiles}
import fr.damienraymond.sit.domain.model.{FileChanged, branch, change}
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository, FileLastUpdateRepository, TrackedFilesRepository}
import fr.damienraymond.sit.domain.service.change.{IdentifyUpdatedFile, _}
import fr.damienraymond.sit.infrastructure.repository.{InMemoryCommitRepository, InMemoryCurrentBranchRepository, InMemoryFileLastUpdateRepository, InMemoryTrackedFilesRepository}
import fr.damienraymond.sit.infrastructure.service.change.{GetFileLastUpdateImplementation, ReadFileServiceImplementation}


trait Instantiated {
  implicit val commitCommandHandler: CommitCommandHandler
}




object Instantiation {

  val instantiate = {
    for {

      currentBranchRepository: CurrentBranchRepository <- InMemoryCurrentBranchRepository.create

      _ <- currentBranchRepository.save(branch.Branch(BranchName("master"), CommitHash("hash1")))

      commitRepository: CommitRepository <- InMemoryCommitRepository.create

      testTxt = Filename("test.txt")
      _ <- commitRepository.save(CommitHash("hash1"), OrphanCommit(Set(
        FileChanged(testTxt, change.Change.fromLineAdded(LinesAdded(
          0 -> "Hello",
          1 -> "world"
        )))
      )))

      trackedFilesRepository: TrackedFilesRepository <- InMemoryTrackedFilesRepository.create
      _ <- trackedFilesRepository.save(TrackedFiles(Set(testTxt)))

      fileLastUpdateRepository: FileLastUpdateRepository <- InMemoryFileLastUpdateRepository.create

      _ <- fileLastUpdateRepository.save(testTxt, FileUpdatedDate(ZonedDateTime.now.minusHours(2)))

      getFileLastUpdate: GetFileLastUpdate = new GetFileLastUpdateImplementation
      readFileService: ReadFileService = ReadFileServiceImplementation
      diffService: DiffService = MyersDiffAlgorithm

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
