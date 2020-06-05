package fr.damienraymond.sit.domain.model.file

case class StagedFiles(files: Set[FilePath]) {
  def addFiles(newFiles: Seq[FilePath]): StagedFiles =
    copy(files = files ++ newFiles.toSet)
}

object StagedFiles{
  val empty: StagedFiles = StagedFiles(Set.empty)
}