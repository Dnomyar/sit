package fr.damienraymond.sit.domain.model.file

import fr.damienraymond.sit.domain.model.change.Change

case class FileChanged(filename: FilePath, change: Change)
