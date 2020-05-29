package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.{FilePath, FileStatus}
import fr.damienraymond.sit.domain.repository.FileLastUpdateRepository
import fr.damienraymond.sit.domain.service.change.GetFileLastUpdate.FileNotFound
import fr.damienraymond.sit.domain.service.change.IdentifyUpdatedFile.FileDoesNotExists
import zio.ZIO





class IdentifyUpdatedFile(fileLastUpdateRepository: FileLastUpdateRepository, getFileLastUpdate: GetFileLastUpdate) {

  def identifyFileStatus(filename: FilePath): ZIO[Any, Exception, FileStatus] = {
    (fileLastUpdateRepository.get(filename) <*> getFileLastUpdate.getFileLastUpdate(filename)).flatMap{
      case (None, Left(FileNotFound)) =>
        ZIO.fail(FileDoesNotExists)
      case (Some(lastKnownUpdateDate), Right(actualLastUpdateFile)) if lastKnownUpdateDate == actualLastUpdateFile =>
        ZIO.succeed(FileStatus.Unchanged)
      case (Some(_), Right(_)) =>
        ZIO.succeed(FileStatus.Updated)
      case (None, Right(_)) =>
        ZIO.succeed(FileStatus.Added)
      case (Some(_), Left(_)) =>
        ZIO.succeed(FileStatus.Deleted)
    }
  }

  def identifyUpdatedFile(filename: FilePath): ZIO[Any, Exception, Boolean] =
    identifyFileStatus(filename).map{
      case FileStatus.Updated => true
      case _ => false
    }

}

object IdentifyUpdatedFile {

  sealed trait IdentifyUpdatedFileError extends Exception

  case class LastKnownUpdateNotFound() extends IdentifyUpdatedFileError
  case object FileDoesNotExists extends IdentifyUpdatedFileError

}
