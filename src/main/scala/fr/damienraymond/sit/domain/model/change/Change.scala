package fr.damienraymond.sit.domain.model.change

import fr.damienraymond.sit.domain.model.change.datastructure.IndexedList

import scala.collection.immutable.SortedMap

case class Change(lineRemoved: LinesRemoved, lineAdded: LinesAdded)

object Change {

  val empty: Change = Change(LinesRemoved.empty, LinesAdded.empty)

  def applyChanges(changes: List[Change]): SortedMap[Int, String] =
    changes.foldLeft(IndexedList.empty[String]) {
      case (fileAcc, change) =>

        val removeLines: IndexedList[String] => IndexedList[String] =
          change.lineRemoved.lines.foldLeft(_)(_.delete(_))

        val addLines: IndexedList[String] => IndexedList[String] =
          change.lineAdded.lines.foldLeft(_) {
            case (file, (lineNumber, line)) => file.insertAt(lineNumber, line)
          }

        (removeLines andThen addLines) (fileAcc)
    }.asSortedMap


  def diff(changes: List[Change]): String = ???

}