package lambdaconf.typeclasses

import matryoshka._
import monocle._
import scalaz._

import Scalaz._

object Misc {

  //map(fa)(id) == fa
  //map(fa)(f.compose(g)) == map(map(fa)(g), f)
  //List, Option, Future, Either when partially applied to 1 type parameter
  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }
  object Functor {
    implicit val optionFunctor: Functor[Option] = new Functor[Option] {
      def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
        case None => None
        case Some(value) => Some(f(value))
      }
    }
  }

  trait Apply[F[_]] extends Functor[F[_]] {
    def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)]

    // def ap[A, B](fab: F[A => B], fa: F[A]): F[B] =
    //   map(zip(fab, fa))(t=> t._1(t._2))
  }
  object Apply {
    implicit val applyOption: Apply[Option] = new Apply[Option] {
      override def zip[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] = (fa, fb) match {
        case (Some(a), Some(b)) => Some((a, b))
        case (_, _) => None
      }
    }
  }

  //List, Future (examples)
  trait Applicative[F[_]] extends Apply[F] {
    def pure[A](a: A): F[A]
  }

  trait Monad[F[_]] extends Apply[F] {
    def join[A](ffa: F[F[A]]): F[A]
    def bind[A, B](fa: F[A])(f: A => F[B]): F[B] = join(map(fa)(f))
  }

  trait Partitionable[F[_]] {
    //there are many ways in which this can go worng
    //def partition[A](fa: F[A], p: A => Boolean): (F[A], F[A])
    def partition[A, B, C](fa: F[A], p: A => Either[B, C]): (F[B], F[C])
  }
  //Partitionable: (* => *) => *
  object Partitionable {
    def apply[F[_]](implicit P: Partitionable[F]) = P

    implicit val partitionableList: Partitionable[List]= new Partitionable[List] {
      def partition[A, B, C](fa: List[A], p: A => Either[B, C]): (List[B], List[C]) = {
        val ps = fa.map(p)
        (ps.collect {case Left(l) => l},
        ps.collect {case Right(r) => r})
      }
    }
    //(1:: 2:: 3::Nil).partition(a => Right(a))
    //Partitionable[List].partition(1:: 2:: 3::Nil, a => Right(a))
  }
  implicit class PartitionableSyntax[F[_], A](fa: F[A]) extends AnyVal {
    def partition[B, C](f: A => Either[B, C])(implicit P: Partitionable[F]): () = P.partition(fa, f)
  }
  object PartitionExample {
    case class Bogus[A](value: A)
    case class Valid[A](value: A)

    val (valid, bogus) = emails.partition { email =>
      if (email.isValid) Right(email)
      else Left(email)
    }
  }

  //append(a, append(b, c)) == append(append(a,b), c)
  trait Semigroup[A] {
    def append(a1: A, a2: A): A
  }

  object Semigroup {
    def apply[A](implicit S: Semigroup[A]): Semigroup[A] = S

    implicit val stringSemigroup: Semigroup[String] = new Semigroup[String] {
      override def append(a1: String, a2: String): String = a1 + a2
    }
    implicit val intSemigroup: Semigroup[Int] = new Semigroup[Int] {
      override def append(a1: Int, a2: Int): Int = a1 + a2
    }

    implicit def numericSemigroup[A: Numeric]: Semigroup[A] = new Semigroup[A] {
      override def append(a1: A, a2: A): A = implicitly[Numeric[A]].plus(a1, a2)
    }
  }

  implicit class SemigroupSyntax[A](a: A) extends AnyVal {
    def <>(that: A)(implicit S: Semigroup[A]): A = S.append(a, that.a)
  }

  object Example {
    1 <> 2
    1.1 <> 1.1
    "foo" <> "bar"
  }

  //append(a, append(b, c)) == append(append(a,b), c)
  //append(empty, a) == a
  //append(a, empty) == a
  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    implicit val stringMonoid: Monoid[String] {
      def append(a1: String, a2: String): String = a1 <> a2
      def empty = ""
    }
  }

  //def roundTrip[A: StringSeri](a: A) = a.serialize.deserialize[A] must beRight(a)
  //def roundTrip1[A: StringSeri](a: A) = (a.serialize.deserialize[A] .map(_.serialize)) must beRight(a.serialize)
  trait StringSeri[A] {
    def serialize(a: A): String

    def deserialize(v: String): Either[String, A]
  }

  trait Show[A] { //typeclass that accepts 1 type parameter
    def show(a: A): String
  }

  object Show {
    def apply[A](implicit S: Show[A]): Show[A] = S //we can call show as if it were a function

    implicit val showInt: Show[Int] = new Show[Int] {
      //def showFuncion[A, B]: Show[A => B] = ???
      def show(a: Int): String = a.toString
    }
    implicit val showString: Show[String] = new Show[String] {
      def show(v: String): String = s"""$v"""
    }
    //implicit def showList[A](implicit S: Show[A]): Show[List[A]] {
    //def show(v: List[A]): String = v.map(S.show(_)).mkString("List(" + )
    //}

    //showList[Int].show(1::2::Nil)
    Show[Int].show(2)
    Show[List[Int]].show(1 :: Nil)
  }

  case class Box[A](value: A)

  object Box {
    def showBox[A](implicit S: Show[A]) = new Show[Box[A]] {
      def show(v: Box[A]): String = "box(" + S.show(v.value) + ")"
    }
  }

  /*
implicit case class ShowSyntax[A: Show](value: A) {
  def show: String = Show[A].show(value)
}
1.show
(1::2::3::Nil).show
*/

  //showInt.show(1) : String
}

object exercise1 {
  sealed trait PathLike[A] {
    // ???
  }
  object PathLike {
    def apply[A: PathLike]: PathLike[A] = implicitly[PathLike[A]]
  }
}

object exercise2 {
  import exercise2._
}

object exercise3 {
  import exercise1._
  import exercise2._

  sealed trait Node
  final case object Root extends Node
  final case class Child(parent: Node, name: String) extends Node

  //Define a monoid for this
  implicit val MonoidNode: Monoid[Node] = new Monoid[Node] {
    def append(v1: Node, v2: Node): Node = v2 match {
      case Root => v1
      case Child(parent, name) => Child(append(v1, parent), name)
    }
    def zero: Node = Root
  }


}

object exercise4 {
  import exercise1._
  import exercise2._
  import exercise3._

  implicit class PathLikeSyntax[A: PathLike](self: A) {
    // ???
  }
}
