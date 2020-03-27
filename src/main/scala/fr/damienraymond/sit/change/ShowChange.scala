package fr.damienraymond.sit.change

import fr.damienraymond.sit.change.datastructure.IndexedList

import scala.collection.immutable.SortedSet

object ShowChange {

  def show(init: String, change: Change): String = {
    val lines: IndexedList[String] = IndexedList.fromString(init)

    val removedLines: IndexedList[String] => IndexedList[String] = change.lineRemoved.lines.foldLeft(_) {
      case (linesAcc, lineToRemove) =>
        linesAcc.update(lineToRemove, line => s"-$line")
    }

    def groupByConsecutiveValues(lineRemoved: List[Int], acc: List[SortedSet[Int]]): List[SortedSet[Int]] = {

      def loop(list: List[Int], acc: SortedSet[Int]): (List[Int], SortedSet[Int]) = {
        list match {
          case current :: next :: tail if current + 1 == next => loop(next :: tail, acc + current)
          case current :: next :: tail => (next :: tail, acc + current)
          case current :: Nil => loop(Nil, acc + current)
          case _ => (list, acc)
        }
      }

      loop(lineRemoved, SortedSet.empty) match {
        case (Nil, acc2) => acc :+ acc2
        case (rest, acc2) => groupByConsecutiveValues(rest, acc :+ acc2)
      }
    }

    val groupedByConsecutiveValues = groupByConsecutiveValues(change.lineRemoved.lines.toList.sorted, List.empty)

    val numberOfRemovedLineLowerThan: Int => Int = { n =>
      groupedByConsecutiveValues.takeWhile { group =>
        group.exists(_ <= n) || group.maxOption.exists(_ < n)
      }.flatten.size
    }

    val addedLines: IndexedList[String] => IndexedList[String] = change.lineAdded.lines.foldLeft(_) {
      case (linesAcc, (lineIdx, line)) =>
        val i = numberOfRemovedLineLowerThan(lineIdx)
        val idx = lineIdx + i
        linesAcc.insertAt(idx, s"+$line")
    }

    (removedLines andThen addedLines) (lines)
      .asSortedMap
      .values
      .mkString("\n")

  }

}
