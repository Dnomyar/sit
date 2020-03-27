package fr.damienraymond.sit

import scala.collection.immutable.SortedMap

trait IndexedList[T] {
  def delete(idx: Int): IndexedList[T]

  def insertAt(idx: Int, t: T): IndexedList[T]

  def asSortedMap: SortedMap[Int, T]
}

// List is probably not the right data structure in that case
private class ListIndexedList[T](list: List[T]) extends IndexedList[T] {
  override def delete(idx: Int): IndexedList[T] = {
    val (start, end) = list.splitAt(idx)
    new ListIndexedList(start ++ end.drop(1))
  }

  override def insertAt(idx: Int, t: T): IndexedList[T] = {
    val (start, end) = list.splitAt(idx)
    new ListIndexedList(start ++ (t :: end))
  }

  override val asSortedMap: SortedMap[Int, T] = SortedMap.from(LazyList.from(0).zip(list))
}


object IndexedList {
  def empty[T]: IndexedList[T] = new ListIndexedList[T](Nil)

  def fromList[T](list: List[T]): IndexedList[T] = new ListIndexedList(list)
}