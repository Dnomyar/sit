package fr.damienraymond.sit.change

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.SortedSet

class LinesRemovedSpec extends AnyFlatSpec with Matchers {

  behavior of "groupedByConsecutiveValues"

  it should "return empty when there is no line removed" in {
    LinesRemoved.empty.groupedByConsecutiveValues should be(empty)
  }

  it should "return a single group" in {
    LinesRemoved(1).groupedByConsecutiveValues should contain only SortedSet(1)
    LinesRemoved(1, 2).groupedByConsecutiveValues should contain only SortedSet(1, 2)
  }


  it should "return several groups" in {
    LinesRemoved(1, 3, 5).groupedByConsecutiveValues should contain inOrderOnly(
      SortedSet(1), SortedSet(3), SortedSet(5)
    )
    LinesRemoved(1, 2, 4, 5).groupedByConsecutiveValues should contain inOrderOnly(
      SortedSet(1, 2), SortedSet(4, 5)
    )
  }

}
