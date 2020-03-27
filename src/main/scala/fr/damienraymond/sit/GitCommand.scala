package fr.damienraymond.sit

sealed trait GitCommand

case class CommitCommand(name: String, changes: Set[FileChanged]) extends GitCommand


sealed trait GitEvent


sealed trait CommittedEvent extends GitEvent

case class Committed(hash: String, parentHash: String) extends CommittedEvent
case class FirstCommitted(hash: String) extends CommittedEvent