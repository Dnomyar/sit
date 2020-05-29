package fr.damienraymond.sit.domain.model.file

sealed trait FileStatus

object FileStatus {

  case object Added extends FileStatus
  case object Updated extends FileStatus
  case object Deleted extends FileStatus
  case object Unchanged extends FileStatus

}