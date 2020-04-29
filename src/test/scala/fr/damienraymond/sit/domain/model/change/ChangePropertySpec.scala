package fr.damienraymond.sit.domain.model.change

import org.scalacheck.{Arbitrary, Gen, Prop, Properties}

class ChangePropertySpec extends Properties("Change") {

  property("applyChange(List(changeA, changeB) === applyChange(List(changeA)) andThen applyChange(List(changeB))") = {

    val genIntStringTuple: Gen[(Int, String)] = for {
      posNum <- Gen.posNum[Int]
      alphaStr <- Gen.alphaStr
    } yield (posNum, alphaStr)

    implicit val arbitraryChange: Arbitrary[Change] = Arbitrary(for {
      posNums <- Gen.listOf(Gen.posNum[Int])
      intStringTuple <- Gen.listOf(genIntStringTuple)
    } yield Change(LinesRemoved(posNums: _*), LinesAdded(intStringTuple: _*)))

    Prop.forAll { (changeA: Change, changeB: Change) =>

      Change.applyChanges(List(changeA, changeB)).asSortedMap == {
        val indexedSeqA = Change.applyChanges(List(changeA))
        Change.applyChanges(List(changeB), zero = indexedSeqA)
      }.asSortedMap

    }


  }

//  property("applyChange(Change(LinesRemoved(TreeSet(0, 2, 3, 4, 5)),LinesAdded(TreeMap())), TreeMap(0 -> , 1 -> , 2 -> , 3 -> , 4 -> :, 5 -> )) == \"\"") = {
//    Change.applyChanges(
//      List(Change(LinesRemoved(0, 2, 3, 4, 5), LinesAdded.empty)),
//      IndexedList.fromList(List("", "", "", "", ":", ""))
//    ).asSortedMap == SortedMap.from(List(0 -> ""))
//  }

}
