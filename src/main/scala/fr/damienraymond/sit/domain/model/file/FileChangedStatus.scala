package fr.damienraymond.sit.domain.model.file

sealed trait FileChangedStatus

object FileChangedStatus {
  case object Added extends FileChangedStatus
  case object Updated extends FileChangedStatus
  case object Deleted extends FileChangedStatus
  case object Unchanged extends FileChangedStatus
}