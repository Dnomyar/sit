package fr.damienraymond.sit.domain.service.change

import cats.Monoid
import cats.implicits._
import fr.damienraymond.sit.domain.model.change.{Change, LinesAdded, LinesRemoved}

import scala.collection.immutable.SortedMap

object MyersExperimentationCoord extends App {
  val file1: List[String] = "ABCABBA".split("").toList
  val file2: List[String] = "CBABAC".split("").toList


  val f1Map: Map[Int, String] = LazyList.from(0).zip(file1).toMap
  val f2Map: Map[Int, String] = LazyList.from(0).zip(file2).toMap


  type Coord = (Int, Int)

  val expectedCoord = (f1Map.size - 1, f2Map.size - 1)

  println(expectedCoord)

  //  @scala.annotation.tailrec
  def loop(currents: Set[(Int, List[Coord])]): Set[(Int, List[Coord])] = {

    val results = currents.filter(_._2.headOption.exists(_ == expectedCoord))

    if (results.nonEmpty) results
    else {
      val explorationResult =
        currents.flatMap {
          case (cost, history) =>
            explore(cost, history)
        }

      if (explorationResult.isEmpty) currents
      else loop(explorationResult)
    }


  }

  def explore(cost: Int, history: List[Coord]): Set[(Int, List[Coord])] = history match {
    case (x, y) :: _ =>
      (f1Map.get(x), f2Map.get(y)).mapN {
        case (xV, yV) if xV == yV => // is a diagonal
          loop(Set((cost, (x + 1, y + 1) :: history)))
        case _ =>
          loop(Set(
            (cost + 1, (x, y + 1) :: history),
            (cost + 1, (x + 1, y) :: history)
          ))
      }.getOrElse(Set.empty)

    case Nil =>
      throw new Exception("A starting point should be provided")
  }


  println(
    loop(Set((0, List((0, 0)))))
      .toList
      .sortBy(_._1)
      .headOption
  )


}

object MyersExperimentation extends App {

  //  val file1: List[String] = "ABCABBA".split("").toList
  //  val file2: List[String] = "CBABAC".split("").toList


  sealed trait Op

  case class Rem(t: String, idx: Int) extends Op

  case class Add(t: String, idx: Int) extends Op

  case class Keep(t: String) extends Op

  type Coord = (Int, Int)


  def myers(original: SortedMap[Int, String], newVersion: SortedMap[Int, String]): Option[(Int, List[((Int, Int), Op)])] = {

    val expectedCoord = (original.size - 1, newVersion.size - 1)

    @scala.annotation.tailrec
    def loop(currents: Set[(Int, List[(Coord, Op)])]): Set[(Int, List[(Coord, Op)])] = {

      val results = currents.filter(_._2.headOption.exists(_._1 == expectedCoord))

      if (results.nonEmpty) results
      else {
        val explorationResult =
          currents.flatMap {
            case (cost, history) =>
              explore(cost, history)
          }

        if (explorationResult.isEmpty) currents
        else loop(explorationResult)
      }


    }

    def explore(cost: Int, history: List[(Coord, Op)]): Set[(Int, List[(Coord, Op)])] = history match {
      case ((x, y), _) :: _ =>
        (original.get(x), newVersion.get(y)).mapN {
          case (xV, yV) if xV == yV => // is a diagonal
            loop(Set((cost, ((x + 1, y + 1), Keep(xV)) :: history)))
          case (xV, yV) =>
            loop(Set(
              (cost + 1, ((x, y + 1), Add(yV, y)) :: history),
              (cost + 1, ((x + 1, y), Rem(xV, x)) :: history)
            ))
        }.getOrElse(Set.empty)

      case Nil =>
        throw new Exception("A starting point should be provided")
    }


    println("(x,y) where x abs and y ord")

    println(s"original = ${original}")
    println(s"newVersion = ${newVersion}")


    loop(Set((0, List(((0, 0), Keep(""))))))
      .toList
      .sortBy(_._1)
      .headOption
  }


  def myers(original: List[String], newVersion: List[String]): Option[(Int, List[((Int, Int), Op)])] = {
    val originalWithIndex: Map[Int, String] = LazyList.from(0).zip(original).toMap
    val newVersionWithIndex: Map[Int, String] = LazyList.from(0).zip(newVersion).toMap
    myers(SortedMap.from(originalWithIndex), SortedMap.from(newVersionWithIndex))
  }


  def myersTest(original: String, newVersion: String): Option[(Int, List[((Int, Int), Op)])] =
    myers(original.split("").toList, newVersion.split("").toList)


  private val original = "ABCABBA"
  private val newVersion = "CBABAC"
  val res = myersTest(original, newVersion)


  implicit val monoidLineRemoved = new Monoid[LinesRemoved] {
    override def empty: LinesRemoved = LinesRemoved.empty

    override def combine(x: LinesRemoved, y: LinesRemoved): LinesRemoved =
      LinesRemoved(x.lines ++ y.lines)
  }

  implicit val monoidLinesAdded = new Monoid[LinesAdded] {
    override def empty: LinesAdded = LinesAdded.empty

    override def combine(x: LinesAdded, y: LinesAdded): LinesAdded =
      LinesAdded(x.lines ++ y.lines)
  }

  implicit val monoidChange = new Monoid[Change] {
    override def empty: Change = Change.empty

    override def combine(x: Change, y: Change): Change =
      Change(
        Monoid.combine(x.lineRemoved, y.lineRemoved),
        Monoid.combine(x.lineAdded, y.lineAdded),
      )
  }


  val ops: List[Op] = res.map(_._2.map(_._2)).toList.flatten.reverse

  val change =
    ops
      .collect {
        case Rem(t, idx) => Change.fromLineRemoved(LinesRemoved(idx))
        case Add(t, idx) => Change.fromLineAdded(LinesAdded(idx -> t))
      }
      .foldLeft(Monoid[Change].empty)(Monoid[Change].combine)


  println(s"Change = $change")

  println(ShowChangeService.show(original.split("").mkString("\n"), change))


//
//  val r = ops
//    .foldRight(List.empty[String]) {
//      case (Keep(line), acc) => s" $line" :: acc
//      case (Rem(line, _), acc) => s"-$line" :: acc
//      case (Add(line, _), acc) => s"+$line" :: acc
//    }
//
//  println(res)
//  println(r.mkString("\n"))


}

