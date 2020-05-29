package fr.damienraymond.sit.domain.service.change

import java.time.ZonedDateTime

import fr.damienraymond.sit.domain.model.file.{FilePath, FileStatus, FileUpdatedDate}
import fr.damienraymond.sit.domain.repository.FileLastUpdateRepository
import fr.damienraymond.sit.domain.service.change.GetFileLastUpdate.FileNotFound
import fr.damienraymond.sit.domain.service.change.IdentifyUpdatedFile.FileDoesNotExists
import org.mockito.MockitoSugar
import zio.ZIO
import zio.test.Assertion._
import zio.test._

object IdentifyUpdatedFileSpec extends DefaultRunnableSpec with MockitoSugar {
  override def spec = suite("IdentifyUpdatedFile")(
    testM("should return FileDoesNotExists"){
      val fileLastUpdateRepository = mock[FileLastUpdateRepository]
      val getFileLastUpdate = mock[GetFileLastUpdate]
      val identifyUpdatedFile = new IdentifyUpdatedFile(fileLastUpdateRepository, getFileLastUpdate)
      val filename = FilePath("test.txt")

      when(fileLastUpdateRepository.get(filename)).thenReturn(ZIO.none)
      when(getFileLastUpdate.getFileLastUpdate(filename)).thenReturn(ZIO.left(FileNotFound))

      for {
        fileStatus <- identifyUpdatedFile.identifyFileStatus(filename).either
      } yield assert(fileStatus)(equalTo(Left(FileDoesNotExists)))
    },
    testM("should not ignore not changes files"){
      val fileLastUpdateRepository = mock[FileLastUpdateRepository]
      val getFileLastUpdate = mock[GetFileLastUpdate]
      val identifyUpdatedFile = new IdentifyUpdatedFile(fileLastUpdateRepository, getFileLastUpdate)
      val filename = FilePath("test.txt")

      val sameDate = FileUpdatedDate(ZonedDateTime.now)
      when(fileLastUpdateRepository.get(filename)).thenReturn(ZIO.some(sameDate))
      when(getFileLastUpdate.getFileLastUpdate(filename)).thenReturn(ZIO.right(sameDate))

      for {
        fileStatus <- identifyUpdatedFile.identifyFileStatus(filename)
      } yield assert(fileStatus)(equalTo(FileStatus.Unchanged))
    },
    testM("should identify updated file"){
      val fileLastUpdateRepository = mock[FileLastUpdateRepository]
      val getFileLastUpdate = mock[GetFileLastUpdate]
      val identifyUpdatedFile = new IdentifyUpdatedFile(fileLastUpdateRepository, getFileLastUpdate)
      val filename = FilePath("test.txt")

      val earlier = FileUpdatedDate(ZonedDateTime.now.minusHours(1))
      val now = FileUpdatedDate(ZonedDateTime.now)

      when(fileLastUpdateRepository.get(filename)).thenReturn(ZIO.some(earlier))
      when(getFileLastUpdate.getFileLastUpdate(filename)).thenReturn(ZIO.right(now))

      for {
        fileStatus <- identifyUpdatedFile.identifyFileStatus(filename)
      } yield assert(fileStatus)(equalTo(FileStatus.Updated))
    },
    testM("should identify added file"){
      val fileLastUpdateRepository = mock[FileLastUpdateRepository]
      val getFileLastUpdate = mock[GetFileLastUpdate]
      val identifyUpdatedFile = new IdentifyUpdatedFile(fileLastUpdateRepository, getFileLastUpdate)
      val filename = FilePath("test.txt")

      val now = FileUpdatedDate(ZonedDateTime.now)

      when(fileLastUpdateRepository.get(filename)).thenReturn(ZIO.none)
      when(getFileLastUpdate.getFileLastUpdate(filename)).thenReturn(ZIO.right(now))

      for {
        fileStatus <- identifyUpdatedFile.identifyFileStatus(filename)
      } yield assert(fileStatus)(equalTo(FileStatus.Added))
    },
    testM("should identify deleted file"){
      val fileLastUpdateRepository = mock[FileLastUpdateRepository]
      val getFileLastUpdate = mock[GetFileLastUpdate]
      val identifyUpdatedFile = new IdentifyUpdatedFile(fileLastUpdateRepository, getFileLastUpdate)
      val filename = FilePath("test.txt")

      val now = FileUpdatedDate(ZonedDateTime.now)

      when(fileLastUpdateRepository.get(filename)).thenReturn(ZIO.some(now))
      when(getFileLastUpdate.getFileLastUpdate(filename)).thenReturn(ZIO.left(GetFileLastUpdate.FileNotFound))

      for {
        fileStatus <- identifyUpdatedFile.identifyFileStatus(filename)
      } yield assert(fileStatus)(equalTo(FileStatus.Deleted))
    }
  )
}
