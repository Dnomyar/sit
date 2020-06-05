package fr.damienraymond.sit.domain.command

import fr.damienraymond.ddd.Event
import fr.damienraymond.ddd.command.CommandHandler
import fr.damienraymond.sit.domain.event.FileAdded
import fr.damienraymond.sit.domain.model.file.StagedFiles
import fr.damienraymond.sit.domain.repository.StagedFilesRepository
import zio.ZIO
import zio.console.Console

class AddCommandHandler(stagedFileRepository: StagedFilesRepository) extends CommandHandler[AddCommand] {
  override def handle(command: AddCommand): ZIO[Console, Exception, Set[Event]] = {
    for {
      stagedFiles <- stagedFileRepository.get
      newStagedFiles = stagedFiles.getOrElse(StagedFiles.empty).addFiles(command.files)
      _ <- stagedFileRepository.save(newStagedFiles)
    } yield Set(FileAdded(newStagedFiles))
  }
}
