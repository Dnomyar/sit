package fr.damienraymond.sit

import java.time.ZonedDateTime

import fr.damienraymond.sit.domain.command.CommitCommandHandler
import fr.damienraymond.sit.domain.model.branch.BranchName
import fr.damienraymond.sit.domain.model.change.LinesAdded
import fr.damienraymond.sit.domain.model.commit.{CommitHash, OrphanCommit}
import fr.damienraymond.sit.domain.model.file.{FileChanged, FilePath, FileUpdatedDate, TrackedFiles}
import fr.damienraymond.sit.domain.model.{branch, change}
import fr.damienraymond.sit.domain.query.StatusQueryHandler
import fr.damienraymond.sit.domain.repository._
import fr.damienraymond.sit.domain.service.change.{IdentifyUpdatedFile, _}
import fr.damienraymond.sit.infrastructure.repository._
import fr.damienraymond.sit.infrastructure.service.change.{FileSystemFilesImplementation, GetFileLastUpdateImplementation, GitIgnoreServiceImplementation, ReadFileServiceImplementation}


trait Instantiated {
  implicit val commitCommandHandler: CommitCommandHandler
  implicit val statusQueryHandler: StatusQueryHandler
}




object Instantiation {

  val instantiate = {
    for {

      currentBranchRepository: CurrentBranchRepository <- InMemoryCurrentBranchRepository.create

      _ <- currentBranchRepository.save(branch.Branch(BranchName("master"), CommitHash("hash1")))

      commitRepository: CommitRepository <- InMemoryCommitRepository.create

      testTxt = FilePath("test.txt")
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

      fileSystemFiles: FileSystemFiles = FileSystemFilesImplementation

      stagedFileRepository: StagedFilesRepository <- InMemoryStagedFilesRepository.create

//      _ <- stagedFileRepository.save(file.StagedFiles(Set(testTxt)))

      identifyUpdatedFile: IdentifyUpdatedFile = new IdentifyUpdatedFile(fileLastUpdateRepository, getFileLastUpdate)
      identifyChangesService: IdentifyChangesService = new IdentifyChangesService(
        stagedFileRepository,
        trackedFilesRepository,
        identifyUpdatedFile,
        readFileService,
        fileSystemFiles,
        diffService
      )

      ignoreService: IgnoreService <- GitIgnoreServiceImplementation.create(".gitignore")


    } yield new Instantiated {
      override implicit val commitCommandHandler: CommitCommandHandler = new CommitCommandHandler(
        currentBranchRepository,
        commitRepository,
        identifyChangesService
      )
      override implicit val statusQueryHandler: StatusQueryHandler = new StatusQueryHandler(identifyChangesService, ignoreService)
    }
  }

}
