package lambdaconf.types

import matryoshka._
import monocle._
import scalaz._

import Scalaz._

object exercise1 {
  type BoardRow[A] = (A, A, A, A, A, A, A, A)
  type BoardRow2[A, B] = (A, B, A, B, A, B, A, B) // we can have different types according to the colors of the board

  // This will be a Product type
  // Modelling matrix as List[List[CheckerPiece]] is not a safe representation, so we use instead a BoardRow type
  final case class Board8x8[A](matrix: BoardRow[BoardRow[A]])
  final case class Board8x8_2[A, B](matrix: BoardRow2[Board8x8_2[A, B], BoardRow2[B, A]])

  type CheckerBoard = Board8x8[Option[CheckerPiece]]
  type CheckerBoard2 = Board8x8_2[Option[CheckerPiece], Unit]

  // This will be a Sum type
  sealed trait ColoredPiece
  final case object BlackPiece extends ColoredPiece
  final case object WhitePiece extends ColoredPiece

  sealed trait CheckerPiece
  final case class Crowned(piece: ColoredPiece) extends CheckerPiece
  final case class Uncrowned(piece: ColoredPiece) extends CheckerPiece
}

object exercise2 {
  final case class Box[A](/* ??? */)
}

object exercise3 { // explicitly ascribe types to these in kind annotation syntax
  // 1. scala.collection.List : * => *, _[_]

  // 2. F[_, _], (*, *) => *

  // 3. Option: x => x, _[_]

  // 4. Int: *

  // 5. T[_[_], _], (* => *, *) => *

}

object exercise4 {
  trait FileSystem {
    // ???
  }
}

object exercise5 {
  sealed trait Example[F[_]] {
    def value: F[String]
  }

  type MapAInt[A] = Map[A, Int]
  val mapExample: Example[MapAInt] = new Example[MapAInt] {
    def value: MapAInt[String] = ???
  }

  val mapExample1: Example[Map[?, Int]] = new Example[Map[?, Int]] {
    def value: Map[String, Int]  = ???
  }

  type Map2[A] = Map[String, A] // Map[A, String] also works
  //Map[A, A] also works
  //Map[String, String] also works
  val mapExample2: Example[Map2] = new Example[Map2] {
    // Example[Map2]
    // def value: Map2[String]
    // def value: Map[String, String]
    def value: Map[String, String] = ???
  }

  val ExampleOption : Example[Option] = new Example[Option] {
    def value: Option[String] = Some ("I am here")
  }

  val ExampleList: Example[List] = new Example[List] {
    override def value: List[String] = ExampleOption.value.toList
  }

  type EitherAInt[A] = Either[A, Int]

  val ExampleEither: Example[EitherAInt] = new Example[EitherAInt] {
    def value: EitherAInt[String] = Right(2)
  }

  val ExampleEither2: Example[Either[?, Int]] = new Example[Either[?, Int]] {
    def value: Either[String, Int] = Right(2)
  }

}
