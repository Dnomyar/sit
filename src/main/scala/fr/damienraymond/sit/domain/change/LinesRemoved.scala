package fr.damienraymond.sit.domain.change

import scala.collection.immutable.SortedSet

case class LinesRemoved(lines: SortedSet[Int]) {

  lazy val groupedByConsecutiveValues: List[SortedSet[Int]] = {

    @scala.annotation.tailrec
    def groupByConsecutiveValues(lineRemoved: List[Int], currentGroupAcc: SortedSet[Int], groupsAcc: List[SortedSet[Int]]): List[SortedSet[Int]] = {

      val areConsecutiveNumbers: Int => Int => Boolean = currentElement => nextElement =>
        currentElement + 1 == nextElement

      lineRemoved match {

        case currentElement :: nextElement :: tail if areConsecutiveNumbers(currentElement)(nextElement) =>
          val currentGroupWithCurrentElement = currentGroupAcc + currentElement
          groupByConsecutiveValues(nextElement :: tail, currentGroupWithCurrentElement, groupsAcc)

        case head :: tail =>
          val currentGroupWithCurrentElement = currentGroupAcc + head
          val groupsWithNewGroupAccumulated = groupsAcc :+ currentGroupWithCurrentElement
          val newEmptyGroup = SortedSet.empty[Int]
          groupByConsecutiveValues(tail, newEmptyGroup, groupsWithNewGroupAccumulated)

        case Nil => groupsAcc
      }
    }

    groupByConsecutiveValues(lines.toList, SortedSet.empty, List.empty)
  }

}

object LinesRemoved {

  val empty: LinesRemoved = new LinesRemoved(SortedSet.empty)

  def apply(linesNumbers: Int*): LinesRemoved = new LinesRemoved(SortedSet.from(linesNumbers))

}
