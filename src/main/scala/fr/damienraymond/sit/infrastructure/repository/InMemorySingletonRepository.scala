package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.SingletonRepository
import zio.{IO, Ref, ZIO}

abstract class InMemorySingletonRepository[T](ref: Ref[Option[T]]) extends SingletonRepository[T] {

  override def get: IO[repository.RepositoryError, Option[T]] = ref.get

  override def save(value: T): IO[repository.RepositoryError, Unit] = ref.set(Some(value))
}


trait InMemorySingletonRepositoryFactory[T, R] {

  protected def instantiate: (Ref[Option[T]]) => R

  def create: ZIO[Any, Nothing, R] = {
    for {
      ref <- Ref.make(Option.empty[T])
    } yield instantiate(ref)
  }
}

//object InMemorySingletonRepository {
//  def create[T, R](instantiate: (Ref[Option[T]]) => R): ZIO[Any, Nothing, R] = {
//    for {
//      ref <- Ref.make(Option.empty[T])
//    } yield instantiate(ref)
//  }
//
////  def empty[T]: ZIO[Any, Nothing, SingletonRepository[T]] =
////    for {
////      ref <- Ref.make(Option.empty[T])
////    } yield new InMemorySingletonRepository[T](ref)
//}