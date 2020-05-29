package fr.damienraymond.sit.domain.model.file

case class TrackedFiles(files: Set[FilePath])

object TrackedFiles {
  val empty: TrackedFiles = TrackedFiles(Set.empty)
}