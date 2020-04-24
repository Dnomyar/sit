package fr.damienraymond.sit.domain.model.change

import fr.damienraymond.sit.domain.model.change.datastructure.IndexedList

import scala.collection.immutable.SortedMap

case class Change(linesRemoved: LinesRemoved, linesAdded: LinesAdded) {

  def withNewLineRemoved(lineRemoved: Int): Change =
    copy(linesRemoved = linesRemoved.withNewLineRemoved(lineRemoved))

  def withNewLineAdded(idx: Int, line: String): Change =
    copy(linesAdded = linesAdded.withNewLineAdded(idx, line))

}

object Change {

  val empty: Change = Change(LinesRemoved.empty, LinesAdded.empty)

  def fromLineAdded(lineAdded: LinesAdded): Change =
    Change(LinesRemoved.empty, lineAdded)

  def fromLineRemoved(linesRemoved: LinesRemoved): Change =
    Change(linesRemoved, LinesAdded.empty)

  def applyChanges(changes: List[Change]): SortedMap[Int, String] =
    changes.foldLeft(IndexedList.empty[String]) {
      case (fileAcc, change) =>

        val removeLines: IndexedList[String] => IndexedList[String] =
          change.linesRemoved.lines.zipWithIndex.foldLeft(_){
            case (file, (idxToRemove, idx)) => file.delete(idxToRemove - idx)
          }

        val addLines: IndexedList[String] => IndexedList[String] =
          change.linesAdded.lines.foldLeft(_) {
            case (file, (lineNumber, line)) => file.insertAt(lineNumber, line)
          }

        (removeLines andThen addLines) (fileAcc)
    }.asSortedMap


}