package fr.damienraymond.sit.domain.model.change

import scala.collection.immutable.SortedMap

case class LinesAdded(lines: SortedMap[Int, String]) {

  def withNewLineAdded(idx: Int, line: String): LinesAdded =
    LinesAdded(lines + (idx -> line))


}

object LinesAdded {

  val empty: LinesAdded = new LinesAdded(SortedMap.empty)

  def apply(lines: (Int, String)*): LinesAdded = new LinesAdded(SortedMap.from(lines))

}
