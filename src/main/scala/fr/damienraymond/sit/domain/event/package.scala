package fr.damienraymond.sit.domain

import fr.damienraymond.ddd.Event
import fr.damienraymond.sit.domain.model.commit.AbstractCommit
import fr.damienraymond.sit.domain.model.file.StagedFiles

package object event {

  case class FileAdded(stagedFiles: StagedFiles) extends Event

  case class CommitCreated(commit: AbstractCommit) extends Event

}
