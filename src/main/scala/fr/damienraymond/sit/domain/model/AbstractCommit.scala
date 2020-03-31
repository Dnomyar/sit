package fr.damienraymond.sit.domain.model

sealed trait AbstractCommit {
  def hash: CommitHash = CommitHash("todo")
  def changes: Set[FileChanged]
}

case class OrphanCommit(changes: Set[FileChanged]) extends AbstractCommit

case class Commit(parentHash: CommitHash, changes: Set[FileChanged]) extends AbstractCommit

case class MergeCommit(parentHashes: List[CommitHash], changes: Set[FileChanged]) extends AbstractCommit
