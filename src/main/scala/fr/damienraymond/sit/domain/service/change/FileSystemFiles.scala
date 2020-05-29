package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.FilePath
import zio.IO

trait FileSystemFiles {

  def allFiles: IO[Exception, Set[FilePath]]

}
