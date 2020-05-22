package fr.damienraymond.sit.domain

import zio.IO

package object repository {

  sealed trait RepositoryError extends Exception

  trait Repository[ID, T] {
//    def getAll: IO[RepositoryError, List[T]]

    def get(id: ID): IO[RepositoryError, Option[T]]

    def save(id: ID, value: T): IO[RepositoryError, Unit]
  }

  trait SingletonRepository[T] {
    def get: IO[RepositoryError, Option[T]]

    def save(value: T): IO[RepositoryError, Unit]
  }

}
