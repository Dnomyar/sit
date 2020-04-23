package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.Filename
import zio.ZIO

trait ReadFileService {

  def readFile(filename: Filename): ZIO[Any, Exception, Iterable[String]]

}
