package fr.damienraymond.sit.domain.change.datastructure

import scala.collection.immutable.SortedMap

// This data-structure was introduced because at the best of my knowledge, there is not data-structure in Scala that has this logic.
trait IndexedList[T] { self =>
  def get(idx: Int): Option[T]

  def delete(idx: Int): IndexedList[T]

  def insertAt(idx: Int, t: T): IndexedList[T]

  def update(idx: Int, f: T => T): IndexedList[T]

  def asSortedMap: SortedMap[Int, T]
}

// List is probably not the right data structure in that case
private class ListIndexedList[T](list: List[T]) extends IndexedList[T] {

  override def get(idx: Int): Option[T] = list.drop(idx).headOption

  override def delete(idx: Int): IndexedList[T] = {
    val (start, end) = list.splitAt(idx)
    new ListIndexedList(start ++ end.drop(1))
  }

  override def insertAt(idx: Int, t: T): IndexedList[T] = {
    val (start, end) = list.splitAt(idx)
    new ListIndexedList(start ++ (t :: end))
  }

  override def update(idx: Int, f: T => T): IndexedList[T] =
    get(idx)
      .map(f)
      .map(list.updated(idx, _))
      .map(new ListIndexedList(_))
      .getOrElse(this)

  override val asSortedMap: SortedMap[Int, T] = SortedMap.from(LazyList.from(0).zip(list))
}


object IndexedList {
  def empty[T]: IndexedList[T] = new ListIndexedList[T](Nil)

  def fromList[T](list: List[T]): IndexedList[T] = new ListIndexedList(list)

  def fromString(string: String): IndexedList[String] =
    fromList(string.split("\n").toList)
}