package example

import scala.collection.immutable.SortedMap

case class Change(lineRemoved: Set[Int], lineAdded: SortedMap[Int, String])

object Change {

  val empty: Change = Change(Set.empty, SortedMap.empty)

  def applyChanges(changes: List[Change]): SortedMap[Int, String] =
    changes.foldLeft(IndexedList.empty[String]) {
      case (fileAcc, change) =>

        val removeLines: IndexedList[String] => IndexedList[String] =
          change.lineRemoved.foldLeft(_)(_.delete(_))

        val addLines: IndexedList[String] => IndexedList[String] =
          change.lineAdded.foldLeft(_) {
            case (file, (lineNumber, line)) => file.insertAt(lineNumber, line)
          }

        (removeLines andThen addLines) (fileAcc)
    }.asSortedMap


}