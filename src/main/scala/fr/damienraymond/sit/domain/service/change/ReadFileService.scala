package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.FilePath
import zio.ZIO

trait ReadFileService {

  def readFile(filename: FilePath): ZIO[Any, Exception, Iterable[String]]

}
