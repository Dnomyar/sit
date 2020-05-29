package fr.damienraymond.sit.domain.model.file

case class FilePath private (path: String)


object FilePath {
  def apply(path: String): FilePath = {
    val charToDropAtTheBeginning = Set('.', '/')
    new FilePath(path.dropWhile(charToDropAtTheBeginning.contains))
  }
}