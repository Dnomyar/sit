package example

case class FileChanged(filename: String, change: Change)


object FileChanged {

  def changes(fileContent: String, previousChange: Option[Change]): Change = {
    ???
  }

  private def newFile(fileContent: String): Change = ???
}