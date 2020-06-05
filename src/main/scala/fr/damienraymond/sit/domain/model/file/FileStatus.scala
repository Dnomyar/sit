package fr.damienraymond.sit.domain.model.file

sealed trait FileStatus

object FileStatus {

  case object StagedAdded extends FileStatus
  case object StagedUpdated extends FileStatus
  case object StagedDeleted extends FileStatus
  case object NotStagedDeleted extends FileStatus
  case object NotStagedUpdated extends FileStatus
  case object Untracked extends FileStatus


}