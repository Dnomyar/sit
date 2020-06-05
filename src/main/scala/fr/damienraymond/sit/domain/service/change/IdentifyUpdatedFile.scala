package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.{FileChangedStatus, FilePath}
import fr.damienraymond.sit.domain.repository.FileLastUpdateRepository
import fr.damienraymond.sit.domain.service.change.GetFileLastUpdate.FileNotFound
import fr.damienraymond.sit.domain.service.change.IdentifyUpdatedFile.FileDoesNotExists
import zio.ZIO





class IdentifyUpdatedFile(fileLastUpdateRepository: FileLastUpdateRepository, getFileLastUpdate: GetFileLastUpdate) {

  def identifyFileStatus(filename: FilePath): ZIO[Any, Exception, FileChangedStatus] = {
    (fileLastUpdateRepository.get(filename) <*> getFileLastUpdate.getFileLastUpdate(filename)).flatMap{
      case (None, Left(FileNotFound)) =>
        ZIO.fail(FileDoesNotExists)
      case (Some(lastKnownUpdateDate), Right(actualLastUpdateFile)) if lastKnownUpdateDate == actualLastUpdateFile =>
        ZIO.succeed(FileChangedStatus.Unchanged)
      case (Some(_), Right(_)) =>
        ZIO.succeed(FileChangedStatus.Updated)
      case (None, Right(_)) =>
        ZIO.succeed(FileChangedStatus.Added)
      case (Some(_), Left(_)) =>
        ZIO.succeed(FileChangedStatus.Deleted)
    }
  }

  def identifyUpdatedFile(filename: FilePath): ZIO[Any, Exception, Boolean] =
    identifyFileStatus(filename).map{
      case FileChangedStatus.Updated => true
      case _ => false
    }

}

object IdentifyUpdatedFile {

  sealed trait IdentifyUpdatedFileError extends Exception

  case class LastKnownUpdateNotFound() extends IdentifyUpdatedFileError
  case object FileDoesNotExists extends IdentifyUpdatedFileError

}
