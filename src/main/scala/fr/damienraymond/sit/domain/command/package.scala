package fr.damienraymond.sit.domain

import fr.damienraymond.sit.domain.event.Event
import fr.damienraymond.sit.domain.repository.{CommitRepository, CurrentBranchRepository, FileLastUpdateRepository, TrackedFilesRepository}
import fr.damienraymond.sit.domain.service.change.{DiffService, DummyDiffService, GetFileLastUpdate, IdentifyChangesService, IdentifyUpdatedFile, ReadFileService}
import fr.damienraymond.sit.infrastructure.service.change.ReadFileServiceImplementation
import zio.ZIO

package object command {

  sealed trait Command {
    def handler: CommandHandler[_, _, _]
  }

  case class CommitCommand() extends Command {
    override val handler = new CommitCommandHandler(currentBranchRepository, commitRepository, identifyChangesService)
  }


  trait CommandHandler[ENV, C <: Command, E] {
    def handle(command: C): ZIO[ENV, E, Set[Event]]
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
