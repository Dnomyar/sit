package fr.damienraymond.sit.domain

import fr.damienraymond.ddd.Command
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository, FileLastUpdateRepository, TrackedFilesRepository}
import fr.damienraymond.sit.domain.service.change._
import fr.damienraymond.sit.infrastructure.service.change.ReadFileServiceImplementation

package object command {

  case class CommitCommand() extends Command {
    override val handler = new CommitCommandHandler(currentBranchRepository, commitRepository, identifyChangesService)
  }

  val currentBranchRepository: CurrentBranchRepository = ???
  val commitRepository: CommitRepository = ???
  val trackedFilesRepository: TrackedFilesRepository = ???

  val fileLastUpdateRepository: FileLastUpdateRepository = ???
  val getFileLastUpdate: GetFileLastUpdate = ???
  val readFileService: ReadFileService = ReadFileServiceImplementation
  val diffService: DiffService = DummyDiffService

  val identifyUpdatedFile: IdentifyUpdatedFile = new IdentifyUpdatedFile(fileLastUpdateRepository, getFileLastUpdate)
  val identifyChangesService: IdentifyChangesService = new IdentifyChangesService(
    trackedFilesRepository,
    identifyUpdatedFile,
    readFileService,
    diffService
  )

}
