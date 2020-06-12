package fr.damienraymond.sit.domain.model.commit

import java.security.MessageDigest

import fr.damienraymond.sit.domain.model.file.FileChanged

sealed trait AbstractCommit {
  def hash: CommitHash
  def changes: Set[FileChanged]
}

case class OrphanCommit(changes: Set[FileChanged]) extends AbstractCommit {
  override def hash: CommitHash = CommitHash(MessageDigest.getInstance("MD5").digest(changes.toString().getBytes()).map("%02X".format(_)).mkString)
}

case class Commit(parentHash: CommitHash, changes: Set[FileChanged]) extends AbstractCommit {
  override def hash: CommitHash = CommitHash(MessageDigest.getInstance("MD5").digest(
    (changes.toString() + parentHash.toString).getBytes()
  ).map("%02X".format(_)).mkString)

}

case class MergeCommit(parentHashes: List[CommitHash], changes: Set[FileChanged]) extends AbstractCommit {
  override def hash: CommitHash = CommitHash(MessageDigest.getInstance("MD5").digest(
    (changes.toString() + parentHashes.toString).getBytes()
  ).map("%02X".format(_)).mkString)
}
