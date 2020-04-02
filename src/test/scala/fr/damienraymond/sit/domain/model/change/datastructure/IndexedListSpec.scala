package fr.damienraymond.sit.domain.model.change.datastructure

import org.scalacheck.Prop.propBoolean
import org.scalacheck.{Prop, Properties}

import scala.util.Try

class IndexedListSpec extends Properties("IndexedList") {

  property("is empty") = IndexedList.empty[Int].size == 0

  property("insert a value at index 0 and get the value back") =
    Prop.forAll { (n: Int) =>
      val oneValue = IndexedList.empty[Int].insertAt(0, n)
      oneValue.size == 1 && oneValue.get(0).contains(n)
    }

  property("insert a value at index 0, delete and then get the value") =
    Prop.forAll { (n: Int) =>
      IndexedList.empty[Int].insertAt(0, n).delete(0).get(0).isEmpty
    }

  property("get the right element") =
    Prop.forAll { (seed: List[Int], idx: Int) =>

      val list = IndexedList.fromList(seed)

      (indexInRange(idx, list)) ==>
        (list.get(idx) == Try(seed(idx)).toOption)
    }

  property("update elements") =
    Prop.forAll { (seed: List[Int], idx: Int, plus: Int) =>
      val list = IndexedList.fromList(seed)

      (indexInRange(idx, list)) ==>
        (list.update(idx, _ + plus).get(idx) == list.get(idx).map(_ + plus))
    }

  private def indexInRange(idx: Int, list: IndexedList[Int]): Boolean =
    idx >= 0 && idx < list.size
}
