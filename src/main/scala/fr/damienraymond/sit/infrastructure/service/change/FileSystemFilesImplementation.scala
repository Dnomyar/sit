package fr.damienraymond.sit.infrastructure.service.change

import java.io.File

import fr.damienraymond.sit.domain.model.file.FilePath
import fr.damienraymond.sit.domain.service.change.FileSystemFiles
import zio.IO

object FileSystemFilesImplementation extends FileSystemFiles {

  override def allFiles: IO[Exception, Set[FilePath]] = {

    @scala.annotation.tailrec
    def exploreDirectories(directoriesToExplore: Set[File], files: Set[File]): Set[File] = {
      if (directoriesToExplore.isEmpty) files
      else {
        val (newDirectoriesToExplore, newFiles) =
          directoriesToExplore
            .filter(_.isDirectory)
            .flatMap(_.listFiles.toSet)
            .partition(_.isDirectory)

        exploreDirectories(newDirectoriesToExplore, files ++ newFiles)
      }
    }

    IO(exploreDirectories(Set(new File(".")), Set.empty))
      .flatMap(IO.foreach(_)(f => IO(f.getPath)))
      .map(_.map(FilePath(_)).toSet)
      .mapError(th => new Exception(th))

  }
}
