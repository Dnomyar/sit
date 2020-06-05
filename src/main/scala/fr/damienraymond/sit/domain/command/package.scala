package fr.damienraymond.sit.domain

import fr.damienraymond.ddd.command.Command
import fr.damienraymond.sit.domain.model.file.FilePath

package object command {

  case class AddCommand(files: FilePath*) extends Command
  case class CommitCommand() extends Command

}
