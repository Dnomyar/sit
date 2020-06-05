package fr.damienraymond.sit.infrastructure.service.change

import fr.damienraymond.sit.domain.model.file.FilePath
import fr.damienraymond.sit.domain.service.change.IgnoreService
import zio.{ZIO, ZManaged}

import scala.io.Source

class GitIgnoreServiceImplementation(filesPatterns: Set[String]) extends IgnoreService {

  override def shouldIgnoreFile(filePath: FilePath): Boolean =
    filesPatterns.exists(filePattern => filePath.path.contains(filePattern))

}


object GitIgnoreServiceImplementation {

  val defaultFilePatterns = Set(".git/")

  val create: String => ZIO[Any, Throwable, GitIgnoreServiceImplementation] = (filename: String) => {
    ZManaged.fromAutoCloseable(ZIO(Source.fromFile(filename))).use{ file =>
      for{
        lines <- ZIO(file.getLines())
      } yield new GitIgnoreServiceImplementation(lines.toSet ++ defaultFilePatterns)
    }
  }
}