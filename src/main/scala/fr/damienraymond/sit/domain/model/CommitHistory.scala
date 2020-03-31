package fr.damienraymond.sit.domain.model

import fr.damienraymond.sit.domain.model.change.Change

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


  def newCommit(filesChanges: Set[FileChanged]): AbstractCommit = ???

}


object CommitHistory {

  def apply(commits: AbstractCommit*): CommitHistory = new CommitHistory(commits.toList)

  def applyCommits(commits: CommitHistory): Set[File] = {
    commits.groupChangesByFile
      .view.mapValues(Change.applyChanges)
      .map {
        case (filename, content) => File(filename, content)
      }
      .toSet
  }
}