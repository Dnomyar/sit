package fr.damienraymond.sit.change

import scala.collection.immutable.SortedSet

case class LinesRemoved(lines: SortedSet[Int]) {



}

object LinesRemoved {

  val empty: LinesRemoved = new LinesRemoved(SortedSet.empty)

  def apply(linesNumbers: Int*): LinesRemoved = new LinesRemoved(SortedSet.from(linesNumbers))

}
