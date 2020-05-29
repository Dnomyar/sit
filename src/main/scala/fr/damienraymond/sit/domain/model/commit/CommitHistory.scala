package fr.damienraymond.sit.domain.model.commit

import fr.damienraymond.sit.domain.model.change.Change
import fr.damienraymond.sit.domain.model.file.{File, FileChanged, FilePath}
import fr.damienraymond.sit.domain.model.file

case class CommitHistory private(commits: List[AbstractCommit]) {

  lazy val groupChangesByFile: Map[FilePath, List[Change]] = {

    val accumulateByFile: (Map[FilePath, List[Change]], FileChanged) => Map[FilePath, List[Change]] = {
      case (acc, FileChanged(filename, change)) =>
        val updatedChanges = acc.get(filename).map(_ :+ change).getOrElse(List(change))
        acc.updated(filename, updatedChanges)
    }

    val accumulateChangesByCommit = (acc: Map[FilePath, List[Change]], commit: AbstractCommit) =>
      commit.changes.foldLeft(acc)(accumulateByFile)

    commits.foldLeft(Map.empty[FilePath, List[Change]])(accumulateChangesByCommit)
  }


  def newCommit(filesChanges: Set[FileChanged]): AbstractCommit =
    commits match {
      case latestCommit :: _ => Commit(latestCommit.hash, filesChanges)
      case Nil => OrphanCommit(filesChanges)
    }

}


object CommitHistory {

  def apply(commits: AbstractCommit*): CommitHistory = new CommitHistory(commits.toList)

  def applyCommits(commits: CommitHistory): Set[File] = {
    commits.groupChangesByFile
      .view
      .mapValues((Change.applyChanges(_: List[Change])) andThen (_.asSortedMap))
      .map {
        case (filename, content) => file.File(filename, content)
      }
      .toSet
  }
}