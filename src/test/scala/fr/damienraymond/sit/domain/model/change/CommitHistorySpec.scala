package fr.damienraymond.sit.domain.model.change

import fr.damienraymond.sit.domain.model
import fr.damienraymond.sit.domain.model.commit.{Commit, OrphanCommit}
import fr.damienraymond.sit.domain.model.file.Filename
import fr.damienraymond.sit.domain.model.{Commit, CommitHistory, FileChanged, Filename}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CommitHistorySpec extends AnyFlatSpec with Matchers {

  behavior of "CommitHistory"

  it should "allow to get the changes grouped by file" in {

    val file1 = Filename("fineName1")
    val file2 = Filename("fineName2")

    val commit1File1Change = Change.fromLineAdded(LinesAdded(0 -> "hello", 1 -> "world"))

    val commit1 = OrphanCommit(Set(FileChanged(file1, commit1File1Change)))

    val commit2File1Change = Change(LinesRemoved(1), LinesAdded(1 -> "Damien"))
    val commit2File2Change = Change.fromLineAdded(LinesAdded(0 -> "bonjour"))

    val commit2 = Commit(commit1.hash, Set(
      model.FileChanged(file1, commit2File1Change),
      model.FileChanged(file2, commit2File2Change)
    ))


    val history = CommitHistory(commit1, commit2)
    history.groupChangesByFile should contain only(
      file1 -> List(commit1File1Change, commit2File1Change),
      file2 -> List(commit2File2Change)
    )

  }

}
