package fr.damienraymond.sit.domain.model.commit

import fr.damienraymond.sit.domain.model.change.Change
import fr.damienraymond.sit.domain.model.file.{File, Filename}
import fr.damienraymond.sit.domain.model.{FileChanged, file}

case class CommitHistory private(commits: List[AbstractCommit]) {

  lazy val groupChangesByFile: Map[Filename, List[Change]] = {

    val accumulateByFile: (Map[Filename, List[Change]], FileChanged) => Map[Filename, List[Change]] = {
      case (acc, FileChanged(filename, change)) =>
        val updatedChanges = acc.get(filename).map(_ :+ change).getOrElse(List(change))
        acc.updated(filename, updatedChanges)
    }

    val accumulateChangesByCommit = (acc: Map[Filename, List[Change]], commit: AbstractCommit) =>
      commit.changes.foldLeft(acc)(accumulateByFile)

    commits.foldLeft(Map.empty[Filename, List[Change]])(accumulateChangesByCommit)
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