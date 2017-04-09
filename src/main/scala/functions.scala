package lambdaconf.functions

import matryoshka._
import monocle._
import scalaz._

import Scalaz._

// kind annotation: [F[_]] is equivalent to [F: * => *]
// [F[_, _]] is equivalent to [F: [*, *] => *]
trait Traversable[F[_]] {
  def foldLeft[A, Z](fa: F[A], initial: Z)(f: (Z, A) => Z): Z
}
//if we have a Future instead of F
// def foldLeft[A, Z](fa: Future[A], initial: Z)(f: (Z, A) => Z): Z

//Traversable: (* => *) => *
//this is the equivalent of a higher order function but with types
//Traversable[Int] the compiler complains because type needs to be function, needs to be
//val t: Traversable[Future] = ???

//kind annotation
// T[_[_]] : (* => *) => *
// _[_] : * => *
trait Foo[T[_[_]]]
// Foo : ((* => *) => *) => *
//val f: Foo[Traversable] = ???


object exercise1 {
  // Domain: {Vegetables, Fruits, Meat, Dairy, Eggs}
  // Codomain: {Love, Like, Neutral, Dislike, Hate}
}

object exercise2 {
  val compareStrings: (Char => Char) => (String, String) => Boolean = ???
}

object exercise3 {

  type Error = String

  //domain is string (which is a product type).
  //codomain is Either (sum type) - Left is a product type and Right is a product (tuple)
  type Parser[A] = String => Either[String, (String, A)]

  // functions in scala have to be monomorphic, we need to use methods for type parameters
  // or is a function combinator
  def or[A](left: Parser[A], right: Parser[A]): Parser[A] = (input: String) =>
    left(input) match {
      case Left(_) => right(input)
      case x => x
    }

  object identity {
    // we can call identity as if this is a function, it is a polymorphic method though and not a function
    // in this case A has an implicit type annotation; a: A has an explicit type annotation
    // type parameters are also known as universals
    def apply[A](a: A): A = a
  }

  def gimmeAListUniversal[A]: List[A]  = ???
  //def gimmeAListExistential: List[A] forSome {type A} = 1 :: 2:: Nil
  //gimmeAListExistential.length and there is nothing more we can do with it

  //case class StateMachine[S, A](initial: S, update: S => (S, A))
  //def gimmeStateMachine: StateMachine[S, Int] forSome  {type S} = ???
  //val s = gimmeStateMachine
  trait StateMachine[A] {
    type State
    def initial: State
    def update(old: State): (State, A)
  }

  trait StateMachine1[S, A] {
    def initial: S
    def update(old: S): (S, A)
  }
  trait HandleStateMachine[A, Z] {
    def apply[S](sm: StateMachine1[S, A]): Z
  }
  trait ExistentialStateMachine[A] {
    def apply[Z](h: HandleStateMachine[A, Z]): Z
  }
  val sm: ExistentialStateMachine[Int] = ???
  //skolemization - forcing consumer to handle all possible types
 /* sm.apply[List[Int]](new HandleStateMachine[Int, List[Int]] {
    def apply[S](sm: StateMachine1[S, Int]): List[Int] = {
    val initial: S = sm.initial
    }
  }) */

  //type lambdas: partially applying method to 1 and we get back a function that accepts one less value parameter
  (_ + 1): Int => Int

  trait Traversable[F[_]] {
    def foldLeft[A, Z](fa: F[A], initial: Z)(f: (Z, A) => Z): Z
  }
  object Traversable{
    implicit val TraversableList: Traversable[List] = ???
    //it's not Traversable[Map[A, _]] because Odersky
    //instead we define a structural type - type that is not defined by a name (vs nominal type)
    implicit def TraversableMap[A]: Traversable[({type MapA[B] = Map[A, B]})#MapA] = new Traversable[({type MapA[B] = Map[A, B]})#MapA] {
      override def foldLeft[A, Z](fa: Map[A, A], initial: Z)(f: (Z, A) => Z): Z = ???
    }
    //list.map(_ + 1)
  }

  // passing explicitely a value for the implicit parameter type (but it is not mandatory)
  identity[Int](5)
  // a: Type => a: A => A we can think of polymorphic functions as mathematical functions that take types and return types
  identity(5) // 5
  identity("foo") // "foo"
  identity(1 :: 2 :: Nil) // 1 :: 2 :: Nil
}

object exercise4 {
  //there is only one possible implementation of this
  def snd[A, B](v: (A, B)): B = ???
}
