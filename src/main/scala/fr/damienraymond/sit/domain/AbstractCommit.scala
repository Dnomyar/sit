package fr.damienraymond.sit.domain

sealed trait AbstractCommit

case class OrphanCommit(hash: CommitHash) extends AbstractCommit
case class Commit(hash: CommitHash, parentHash: CommitHash) extends AbstractCommit
case class MergeCommit(hash: CommitHash, parentHashes: List[CommitHash]) extends AbstractCommit
