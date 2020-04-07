package fr.damienraymond.sit.domain

import fr.damienraymond.sit.domain.model.commit.AbstractCommit

package object event {

  sealed trait Event

  case class CommitCreated(commit: AbstractCommit) extends Event

}
