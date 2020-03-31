package fr.damienraymond.sit.domain.change

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.collection.immutable.SortedSet
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

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

//  it should "have the same number of elements than the input" in {
//
//    val genNumber = Gen.choose(0, 10000)
//    val sortedSetGen =
//      Gen
//        .listOfN(genNumber.sample.getOrElse(100), genNumber)
//        .map(SortedSet.from(_))
//
//    forAll(sortedSetGen) { (numbers: SortedSet[Int]) =>
//      LinesRemoved.apply(numbers).groupedByConsecutiveValues.flatten.size should be (numbers.size)
//    }
//
//  }

}
