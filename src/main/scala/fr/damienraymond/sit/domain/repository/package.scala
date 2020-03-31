package fr.damienraymond.sit.domain

import zio.IO

package object repository {

  sealed trait RepositoryError

  trait Repository[ID, T] {
    def getAll: IO[RepositoryError, Option[T]]
    def get(id: ID): IO[RepositoryError, Option[T]]
    def save(id: ID, value: T): IO[RepositoryError, Unit]
  }

  trait SingletonRepository[T] {
    def get: IO[RepositoryError, Option[T]]
    def save: IO[RepositoryError, Unit]
  }

}
