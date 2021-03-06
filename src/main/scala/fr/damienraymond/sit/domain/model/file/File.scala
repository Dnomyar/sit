package fr.damienraymond.sit.domain.model.file

import scala.collection.immutable.SortedMap

case class File(filename: FilePath, content: SortedMap[Int, String])
