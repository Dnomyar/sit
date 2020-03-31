package fr.damienraymond.sit.domain.change

import scala.collection.immutable.SortedMap

case class LinesAdded(lines: SortedMap[Int, String])

object LinesAdded {

  val empty: LinesAdded = new LinesAdded(SortedMap.empty)

  def apply(lines: (Int, String)*): LinesAdded = new LinesAdded(SortedMap.from(lines))

}
