package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.file.FilePath

trait IgnoreService {

  def shouldIgnoreFile(filePath: FilePath): Boolean

  def shouldKeepFile(filePath: FilePath): Boolean =
    ! shouldIgnoreFile(filePath)

}
