package fr.damienraymond.sit.infrastructure.repository

import fr.damienraymond.sit.domain.repository
import fr.damienraymond.sit.domain.repository.Repository
import zio.{IO, Ref, ZIO}

abstract class InMemoryRepository[ID, T](ref: Ref[Map[ID, T]]) extends Repository[ID, T] {

  override def get(id: ID): IO[repository.RepositoryError, Option[T]] =
    ref.get.map(_.get(id))

  override def save(id: ID, value: T): IO[repository.RepositoryError, Unit] =
    ref.update(_.updated(id, value))

}

trait InMemoryRepositoryFactory[ID, T, R] {

  protected def instantiate: (Ref[Map[ID, T]]) => R

  def create: ZIO[Any, Nothing, R] = {
    for {
      ref <- Ref.make(Map.empty[ID, T])
    } yield instantiate(ref)
  }
}
