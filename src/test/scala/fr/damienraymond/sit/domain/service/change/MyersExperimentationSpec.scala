package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.change.{Change, LinesAdded}
import org.scalacheck.{Arbitrary, Gen, Prop, Properties}

import scala.collection.immutable.SortedMap

class MyersExperimentationSpec extends Properties("MyersExperimentation") {

  property("identifying change and then applying it should == `vn+1`") = {

    implicit val sortedMapArbitrary: Arbitrary[SortedMap[Int, String]] = Arbitrary(
      implicitly[Arbitrary[List[String]]]
        .arbitrary
        .map(LazyList.from(0).zip)
        .map(SortedMap.from(_))
    )

    Prop.forAll { (`vn`: SortedMap[Int, String], `vn+1`: SortedMap[Int, String]) =>
      val change: Change = MyersExperimentation.change(`vn`, `vn+1`)

      val initialChange = Change.fromLineAdded(LinesAdded(`vn`))

      val changes = List(initialChange, change)

      Change.applyChanges(changes).asSortedMap == `vn+1`
    }

  }
}
