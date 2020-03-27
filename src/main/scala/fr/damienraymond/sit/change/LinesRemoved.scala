package fr.damienraymond.sit.change

import scala.collection.immutable.SortedSet

case class LinesRemoved(lines: SortedSet[Int]) {

  lazy val groupedByConsecutiveValues: List[SortedSet[Int]] = {
    @scala.annotation.tailrec
    def groupByConsecutiveValues(lineRemoved: List[Int], acc: List[SortedSet[Int]]): List[SortedSet[Int]] = {

      @scala.annotation.tailrec
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
    groupByConsecutiveValues(lines.toList, List.empty)
  }

}

object LinesRemoved {

  val empty: LinesRemoved = new LinesRemoved(SortedSet.empty)

  def apply(linesNumbers: Int*): LinesRemoved = new LinesRemoved(SortedSet.from(linesNumbers))

}
