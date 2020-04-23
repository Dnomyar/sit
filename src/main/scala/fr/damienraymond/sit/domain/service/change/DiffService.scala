package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.change.{Change, LinesAdded, LinesRemoved}

import scala.collection.immutable.{SortedMap, SortedSet}

trait DiffService {

  def identifyChanges(initial: Iterable[String], newVersion: Iterable[String]): Change

}


object DummyDiffService extends DiffService {
  override def identifyChanges(initial: Iterable[String], newVersion: Iterable[String]): Change = {
    val removeInitialFile = LinesRemoved(SortedSet.from(0 until initial.size))
    val addNewVersion = LinesAdded(SortedMap.from(LazyList.from(0).zip(newVersion)))
    Change(removeInitialFile, addNewVersion)
  }
}