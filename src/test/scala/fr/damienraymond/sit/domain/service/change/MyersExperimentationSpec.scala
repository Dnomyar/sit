package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.change.{Change, LinesAdded}
import org.scalacheck.{Prop, Properties}

import scala.collection.immutable.SortedMap

class MyersExperimentationSpec extends Properties("MyersExperimentation") {

  def applyMyers(original: List[String], newVersion: List[String]): Change = {
    val `vn`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(original))
    val `vn+1`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(newVersion))

    applyMyers(`vn`, `vn+1`)
  }

  def applyMyers(`vn`: SortedMap[Int, String], `vn+1`: SortedMap[Int, String]): Change = {
    MyersExperimentation.toChange(MyersExperimentation.myers(`vn`, `vn+1`))
  }


  def applyMyers(original: String, newVersion: String): Change =
    applyMyers(
      original.split("").toList,
      newVersion.split("").toList
    )

//
//  property("same files should produce an empty change") = {
//    val `vn` = "hello world!"
//    val `vn+1` = `vn`
//    applyMyers(`vn`, `vn+1`) == Change.empty
//  }
//
//  property("empty vn and non empty vn+1 should have empty line removed") =
//    forAll { (`vn+1`: String) =>
//      val `vn` = ""
//      applyMyers(`vn`, `vn+1`).lineRemoved.lines.isEmpty
//    }
//
//  property("same number of lines added than the number lines for vn+1 when empty vn is empty") =
//    forAll { (`vn+1`: List[String]) =>
//      val `vn` = List.empty[String]
//      applyMyers(`vn`, `vn+1`).lineAdded.lines.size == `vn+1`.length
//    }
//
//  property("non empty nv and empty vn+1 should have no line added") =
//    forAll { (`vn`: List[String]) =>
//      val `vn+1` = List.empty[String]
//      applyMyers(`vn`, `vn+1`).lineAdded.lines.isEmpty
//    }

  property("test") = Prop.forAll { (v1: String, v2: String) =>
//{
//    val v1 = "ABB"
//    val v2 = "CA"


    val `vn`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(v1.split("")))
    val `vn+1`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(v2.split("")))


    val change: Change = applyMyers(`vn`, `vn+1`)


    val initialChange = Change.fromLineAdded(LinesAdded(`vn`))


    val changes = List(initialChange, change)

    Change.applyChanges(changes) == `vn+1`

//    val `vn` = "ABCABBA".split("").toList
//    val `vn+1` = "CBABAC".split("").toList
//
//    val change = applyMyers(`vn`, `vn+1`)
//
//    val `numberOfLinesVn` = `vn`.groupBy(identity).view.mapValues(_.size).toMap
//    val `numberOfLinesVn+1` = `vn+1`.groupBy(identity).view.mapValues(_.size).toMap
//
//
//    val lineValueRemoved: Set[String] = change.lineRemoved.lines.map(`vn`)
//    val lineValueAdded: Set[String] = change.lineAdded.lines.values.toSet
//
//
//    val expectedSetOfLineRemoved = `vn`.toSet -- `vn+1`.toSet
//    println(lineValueRemoved)
//    println(expectedSetOfLineRemoved)
//
//    lineValueRemoved == expectedSetOfLineRemoved

    //
    //    val changes = List(initialChange, change)
    //
    //    println(changes)
    //
    //    Change.applyChanges(changes) should be(`vn+1`)
  }

  //
  //  it should "have the same number of lines added than the number lines for vn+1" in {
  //    Prop.forAll { (`vn+1`: String) =>
  //      val `vn` = ""
  //      applyMyers(`vn`, `vn+1`).lineAdded.lines.isEmpty
  //    }
  //  }
  //
  //  it should "work" in {
  //
  //    val `vn`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip("ABCABBA".split("")))
  //    val `vn+1`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip("CBABAC".split("")))
  //
  //    val change = MyersExperimentation.toChange(MyersExperimentation.myers(`vn`, `vn+1`))
  //
  //    val initialChange = Change.fromLineAdded(LinesAdded(`vn`))
  //
  //
  //    val changes = List(initialChange, change)
  //
  //    println(changes)
  //
  //    Change.applyChanges(changes) should be (`vn+1`)
  //
  //  }

}
