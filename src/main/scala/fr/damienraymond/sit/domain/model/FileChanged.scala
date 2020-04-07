package fr.damienraymond.sit.domain.model

import fr.damienraymond.sit.domain.model.change.Change
import fr.damienraymond.sit.domain.model.file.Filename

case class FileChanged(filename: Filename, change: Change)


object FileChanged {

  def changes(fileContent: String, previousChange: Option[Change]): Change = {
    ???
  }

  private def newFile(fileContent: String): Change = ???
}