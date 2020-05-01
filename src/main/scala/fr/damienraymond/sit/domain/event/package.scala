package fr.damienraymond.sit.domain

import fr.damienraymond.ddd.Event
import fr.damienraymond.sit.domain.model.commit.AbstractCommit

package object event {

  case class CommitCreated(commit: AbstractCommit) extends Event

}
