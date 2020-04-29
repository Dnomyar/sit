package fr.damienraymond.sit.domain.model.change

import fr.damienraymond.sit.domain.model.change.datastructure.IndexedList
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.SortedMap

class ChangeSpec extends AnyFlatSpec with Matchers {

  behavior of "Change"

  it should "insert a line and shift content" in {
    val changes = List(
      Change(LinesRemoved.empty, LinesAdded(0 -> "hello", 1 -> "world")),
      Change(LinesRemoved.empty, LinesAdded(1 -> "bonjour"))
    )

    Change.applyChanges(changes).asSortedMap should be (SortedMap(
      0 -> "hello",
      1 -> "bonjour",
      2 -> "world"
    ))
  }

  it should "insert a line at the end of the file" in {
    val changes = List(
      Change(LinesRemoved.empty, LinesAdded(0 -> "hello", 1 -> "world")),
      Change(LinesRemoved.empty, LinesAdded(2 -> "bonjour"))
    )

    Change.applyChanges(changes).asSortedMap should be (SortedMap(
      0 -> "hello",
      1 -> "world",
      2 -> "bonjour"
    ))
  }

  it should "replace a line" in {
    val changes = List(
      Change(LinesRemoved.empty, LinesAdded(0 -> "hello", 1 -> "world")),
      Change(LinesRemoved(1), LinesAdded(1 -> "bonjour"))
    )

    Change.applyChanges(changes).asSortedMap should be (SortedMap(
      0 -> "hello",
      1 -> "bonjour"
    ))
  }

  it should "remove the right line" in {
    Change.applyChanges(
      List(Change(LinesRemoved(0, 2, 3, 4, 5), LinesAdded.empty)),
      IndexedList.fromList(List("", "", "", "", ":", ""))
    ).asSortedMap should be (SortedMap.from(List(0 -> "")))
  }


}


