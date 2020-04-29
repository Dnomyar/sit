package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.change.{Change, LinesAdded}
import org.scalacheck.{Arbitrary, Gen, Prop, Properties}

import scala.collection.immutable.SortedMap

class MyersExperimentationSpec extends Properties("MyersExperimentation") {

  def applyMyers(original: List[String], newVersion: List[String]): Change = {
    val `vn`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(original))
    val `vn+1`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(newVersion))

    applyMyers(`vn`, `vn+1`)
  }

  def applyMyers(`vn`: SortedMap[Int, String], `vn+1`: SortedMap[Int, String]): Change = {
    MyersExperimentation.change(`vn`, `vn+1`)
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

  def works(v1: List[String], v2: List[String]): Boolean = {


    val `vn`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(v1))
    val `vn+1`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(v2))


    val change: Change = applyMyers(`vn`, `vn+1`)


    val initialChange = Change.fromLineAdded(LinesAdded(`vn`))


    val changes = List(initialChange, change)

    val res = Change.applyChanges(changes).asSortedMap.values.mkString("\n").trim == `vn+1`.values.mkString("\n").trim

    if(!res){
      println()
      println(s"v1 = `$v1`, v2 = `$v2`")
      println(s"`vn` = `${`vn`}`, `vn+1` = `${`vn+1`}`")
      println(s"initialChange = ${initialChange}")
      val initialChangeApplied = Change.applyChanges(List(initialChange))
      println(s"initialChange applied = ${initialChangeApplied.asSortedMap}")
      println(s"change = ${change}")
      println(s"change applied = ${Change.applyChanges(List(change), initialChangeApplied).asSortedMap}")
      println(s"-${Change.applyChanges(changes).asSortedMap.values.mkString("\n")}-")
      println(s"-${`vn+1`.values.mkString("\n")}-")
      val outcome1 = Change.applyChanges(changes).asSortedMap

      println(outcome1)
      println(`vn+1`)
      println(outcome1.map{
        case (i, str) =>
          val a = `vn+1`(i)
          (str, a, str == a, str == "")
      })

    }

    res

  }

  property("test") = {

    implicit val arbitrary: Arbitrary[String] =
      Arbitrary(
        Gen
          .alphaStr
      )

    Prop.forAll { (v1: List[String], v2: List[String]) => works(v1, v2) }
//    val res = works(List("", "", "", "", "2", ""),  List(""))
//    val res = works(List(""), List("", "", "", "", "2", ""))
//
//    println(res)
//
//    res


//    val v1 = List("", "", "", "", "", "")
//    val v2 = List(":", "", "7")
//
//    val `vn`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(v1))
//    val `vn+1`: SortedMap[Int, String] = SortedMap.from(LazyList.from(0).zip(v2))
//
//
//    val change: Change = applyMyers(`vn`, `vn+1`)
//
//
//    val initialChange = Change.fromLineAdded(LinesAdded(`vn`))
//
//
//    val changes = List(initialChange, change)
//
//    val res = Change.applyChanges(changes).asSortedMap == `vn+1`
//
////    if(!res){
//      println()
//      println(s"v1 = `$v1`, v2 = `$v2`")
//      println(Change.applyChanges(changes).asSortedMap == `vn+1`)
//      println(Change.applyChanges(changes).asSortedMap.values.mkString("\n").trim == `vn+1`.values.mkString("\n").trim)
//      println(s"-${Change.applyChanges(changes).asSortedMap.values.mkString("\n")}-")
//      println(s"-${`vn+1`.values.mkString("\n")}-")
//      println(Change.applyChanges(changes).asSortedMap)
//      println(change)
//      println(`vn+1`)
////    }
//
//    res

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
