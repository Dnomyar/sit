package fr.damienraymond.sit.domain.model

import fr.damienraymond.sit.domain.model.change.Change

case class CommitHistory(commits: List[AbstractCommit]) {

  lazy val groupChangesByFile: Map[Filename, List[Change]] = ???

  def newCommit(filesChanges: Set[FileChanged]): AbstractCommit = ???

}


object CommitHistory {
  def applyCommits(commits: CommitHistory): Set[File] = {
    commits.groupChangesByFile
      .view.mapValues(Change.applyChanges)
      .map {
        case (filename, content) => File(filename, content)
      }
      .toSet
  }
}