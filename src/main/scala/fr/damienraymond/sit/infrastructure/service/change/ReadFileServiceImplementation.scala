package fr.damienraymond.sit.infrastructure.service.change

import java.io.File

import fr.damienraymond.sit.domain.model.file.FilePath
import fr.damienraymond.sit.domain.service.change.ReadFileService
import zio.{IO, Managed, ZIO}

import scala.io.Source

object ReadFileServiceImplementation extends ReadFileService {
  override def readFile(filename: FilePath): ZIO[Any, Exception, Iterable[String]] = {
    val source = IO.effect(Source.fromFile(new File(filename.path)))
    Managed.fromAutoCloseable(source)
      .use(file => ZIO(file.getLines().to(Iterable)))
      .mapError(th => new Exception(th))
  }
}
