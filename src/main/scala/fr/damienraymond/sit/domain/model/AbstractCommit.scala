package fr.damienraymond.sit.domain.model

sealed trait AbstractCommit {
  def hash: CommitHash
  def changes: Set[FileChanged]
}

case class OrphanCommit(hash: CommitHash, changes: Set[FileChanged]) extends AbstractCommit

case class Commit(hash: CommitHash, parentHash: CommitHash, changes: Set[FileChanged]) extends AbstractCommit

case class MergeCommit(hash: CommitHash, parentHashes: List[CommitHash], changes: Set[FileChanged]) extends AbstractCommit

object AbstractCommit {

  def applyCommits(commits: CommitHistory): Set[File] = ???

}