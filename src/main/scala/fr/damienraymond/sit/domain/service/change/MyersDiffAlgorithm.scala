package fr.damienraymond.sit.domain.service.change

import fr.damienraymond.sit.domain.model.change.Change

import scala.collection.immutable.SortedMap


object MyersDiffAlgorithm extends DiffService {


  type Coordinates = (Int, Int)

  type Depth = Int

  type K = Int
  type V = Map[K, Int]
  type History = SortedMap[Depth, V]

  type Trace = List[Coordinates]


  override def identifyChanges(initial: Iterable[String], newVersion: Iterable[String]): Change =
    change(
      SortedMap.from(LazyList.from(0).zip(initial)),
      SortedMap.from(LazyList.from(0).zip(newVersion)),
    )

  def change(original: SortedMap[Int, String], newVersion: SortedMap[Int, String]): Change = {

    val expectedCoordinates = (original.size, newVersion.size)

    val applyChange =
      ((diff _).tupled) andThen
        backtrack(expectedCoordinates) andThen
        backtrackToChange(newVersion)

    applyChange((original, newVersion))
  }


  def diff(original: SortedMap[Int, String], newVersion: SortedMap[Int, String]): History = {
    val `|original|` = original.size
    val `|newVersion|` = newVersion.size

    val max = `|original|` + `|newVersion|`

    val expectedCoord = (`|original|`, `|newVersion|`)

    @scala.annotation.tailrec
    def loop(coord: Coordinates, acc: V, history: History, depth: Int): History =
      if (depth <= max && coord != expectedCoord) {
        val (nCoord, nAcc) = loopDepth(coord, acc, depth, -depth)
        loop(nCoord, nAcc, history.updated(depth, nAcc), depth + 1)
      } else history


    @scala.annotation.tailrec
    def loopDepth(coord: Coordinates, acc: V, depth: Int, k: K): (Coordinates, V) =
      if (k <= depth && coord != expectedCoord) {
        val (nCoord, nAcc) = loopK(depth)(acc, k)
        loopDepth(nCoord, nAcc, depth, k + 2)
      } else (coord, acc)


    def loopK(depth: Int)(acc: V, k: K): (Coordinates, V) = {

      val x = {
        if (k == -depth || (k != depth && acc(k - 1) < acc(k + 1)))
          acc(k + 1)
        else
          acc(k - 1) + 1
      }

      val y = x - k

      val coord@(nx, ny) = loopDiagonal(acc)(x, y)

      (coord, acc.updated(k, nx))

    }

    @scala.annotation.tailrec
    def loopDiagonal(acc: V)(x: Int, y: Int): (Int, Int) =
      if (x < `|original|` && y < `|newVersion|` && original(x) == newVersion(y))
        loopDiagonal(acc)(x + 1, y + 1)
      else
        (x, y)


    loop((0, 0), Map(1 -> 0), SortedMap.empty, 0)
  }

  def backtrack(expectedCoordinates: Coordinates)(history: History): Trace = {

    @scala.annotation.tailrec
    def loop(history: List[(Depth, V)])(trace: Trace)(x: Int, y: Int): Trace = history match {
      case (depth, acc) :: next =>

        val k = x - y

        val prevK =
          if (k == -depth || (k != depth && acc(k - 1) < acc(k + 1))) {
            k + 1
          } else {
            k - 1
          }

        val prevX = acc(prevK)
        val prevY = prevX - prevK

        @scala.annotation.tailrec
        def loopDiagonal(trace: Trace)(x: Int, y: Int): Trace =
          if (x > prevX && y > prevY)
            loopDiagonal((x - 1, y - 1) :: trace)(x - 1, y - 1)
          else
            trace

        val nTrace = loopDiagonal((x, y) :: trace)(x, y)

        loop(next)(nTrace)(prevX, prevY)

      case Nil =>
        trace
    }

    loop(history.toList.reverse)(List.empty)(expectedCoordinates._1, expectedCoordinates._2)


  }

  def backtrackToChange(newVersion: SortedMap[Int, String])(backtrack: Trace): Change = {
    type Vector = (Coordinates, Coordinates)

    def isLeftMove(vector: Vector) = {
      val ((x1, y1), (x2, y2)) = vector
      (x1 < x2) && (y1 == y2)
    }
    def isDownMove(vector: Vector) = {
      val ((x1, y1), (x2, y2)) = vector
      (x1 == x2) && (y1 < y2)
    }

    backtrack.zip(backtrack.drop(1)).foldLeft(Change.empty){
      case (change, v @ ((x, _), _)) if isLeftMove(v) => change.withNewLineRemoved(x)
      case (change, v @ ((_, y), _)) if isDownMove(v) => change.withNewLineAdded(y, newVersion(y))
      case (change, _) => change
    }
  }

}