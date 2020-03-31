package fr.damienraymond.sit.domain.model

case class CommitHistory(commits: List[AbstractCommit]) {

  def newCommit(filesChanges: Set[FileChanged]): AbstractCommit = ???

}
