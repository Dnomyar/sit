package example

sealed trait GitCommand

case class Commit(name: String, changes: Set[FileChanged]) extends GitCommand
