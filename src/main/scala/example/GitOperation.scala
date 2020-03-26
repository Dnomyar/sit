package example

sealed trait GitOperation

case class Commit(name: String, changes: Set[FileChanged]) extends GitOperation
