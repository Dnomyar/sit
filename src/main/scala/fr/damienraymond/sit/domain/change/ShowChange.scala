package fr.damienraymond.sit.domain.change

import fr.damienraymond.sit.domain.change.datastructure.IndexedList

object ShowChange {

  def show(init: String, change: Change): String = {
    val lines: IndexedList[String] = IndexedList.fromString(init)

    val removedLines: IndexedList[String] => IndexedList[String] = change.lineRemoved.lines.foldLeft(_) {
      case (linesAcc, lineToRemove) =>
        linesAcc.update(lineToRemove, line => s"-$line")
    }

    val groupedByConsecutiveValues = change.lineRemoved.groupedByConsecutiveValues

    val numberOfRemovedLineLowerThan: Int => Int = { n =>
      groupedByConsecutiveValues.takeWhile { group =>
        val biggerThanTheGroupMaxValue = group.maxOption.exists(_ < n)
        val nContainedInGroup = group.exists(_ <= n)
        nContainedInGroup || biggerThanTheGroupMaxValue
      }.flatten.size
    }

    val addedLines: IndexedList[String] => IndexedList[String] = change.lineAdded.lines.foldLeft(_) {
      case (linesAcc, (lineIdx, line)) =>
        val idx = lineIdx + numberOfRemovedLineLowerThan(lineIdx)
        linesAcc.insertAt(idx, s"+$line")
    }

    (removedLines andThen addedLines) (lines)
      .asSortedMap
      .values
      .mkString("\n")

  }

}
