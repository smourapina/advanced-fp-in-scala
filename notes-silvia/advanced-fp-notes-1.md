1. Algebraic Data Types

- Make invalid/ illegal states unrepresentable - type is described so precisely that it is impossible to have something else
Example: we model emails as a list of characters - problems: random string that can contain all sorts of stuff. Email abstraction only allows creation of valid emails.
- This eliminates unnecessary error checking. Less bugs, way to reason about programs more reasonable, no need for checking.
- Type definition: a set of values (mathematical set). Example: type of string is the set of all possible strings.
- Function: is a mapping from one type to another (domain to codomain). We build up entire programs by having only types and functions. For every element in domain there is 1 (and only 1) in the codomain. If you feed a function a value, you get the same value - it is deterministic.
- Example of a function that is not total: map.apply (if you feed the wrong key, exception). This is not a mathematical function. Then you end up with a lie, because type signatures are not telling us everything.

- Algebraic Data Type - 2 types (leaf or primitive types and composite types - List for example)
Products and Sums (Coproducts)
i. Product type: type that is a composition of 2 or more types such as it follows AND combination. Example: Adress has a city and a street. Composite type that has multiple components.
ii. Sum: OR, dual of product type. Example - Either, Option, shapes can be modelled (vs in Java we have to model this through relations), animal.

- Case class is a Product
- Trait, inheritance is a Sum. Enums in Dotty will be a proper Sum type.

Exercise: types.scala
(exercise 1)


2. Higher order functions
- Function combinators take a function and return a function.
- State monads are basically just functions. You can build whole programs just composing functions.

Exercise: functions.scala
(exercise 3)


3. Higher-Kinded Types
- Type defined as a set of values, for something to have a type means it is an element of that set.
- Types can also be members of sets. Function defined as a mapping from one type to another, but we can also define them as mapping from types to types (for example Int is a type, List is a type constructor and not a type which is a type-level function). Domain is a set of types, and codomain is another set of types. List is a function from a set of types to a set of types. We apply functions, if we feed a type to list we get back a type (List[Int]), but we call it kind instead.

- Kinds can be thought of as "the type of types".
* — The kind of types (the set of all types).
* => * — The kind of type-level functions that accept 1 type (the set of all type-level functions that accept 1 type and return 1 type). The type constructor List has kind * => *, represented as _[_] in Scala. This is the kind of List, Array, Future. Domain and codomain are a set of types.
[*, *] => * — The kind of type-level functions that accept 2 types (the set of all type-level functions that accept 2 types). The type constructor Either has kind [*, *] => *, represented as _[_, _] in Scala. Example is the BoardRow2 defined in the first exercise, Map.

- Kinds are signatures for type-level functions, analog for type constructors.

- Higher-Order Kinds: equivalent of a higher-order function for types. Example: Functor, Traversable (typeclass for this )

// kind annotation: [F[_]] is equivalent to [F: * => *]
// [F[_, _]] is equivalent to [F: [*, *] => *]
trait Traversable[F[_]] {
  def foldLeft[A, Z](fa: F[A], initial: Z)(f: (Z, A) => Z): Z
}
(types.scala exercise 2)

  def gimmeAListUniversal[A]: List[A]  = ???
  //def gimmeAListExistential: List[A] forSome {type A} = 1 :: 2:: Nil
  //gimmeAListExistential.length and there is nothing more we can do with it

- Skolemization

4. Type Lambdas
- We can partially apply type constructors.
(types, exercise 5)

5. Type classes
- functions exercise 4
dealing with a subset of all possible types and get a set of functionality that we need.

typeclasses.scala
trait Show[A] { //typeclass that accepts 1 type parameter
  def show(a: A): String
}
...

**************************************************************************

6. Semigroups

trait Semigroup[A] {
  def append(a1: A, a2: A) : A
}

typeclasses exercise 3

Monoid: addition, multiplication, events, ...

trait Partitionable...
## Algebraic Data Types

- Make invalid/ illegal states unrepresentable - type is described so precisely that it is impossible to have something else
Example: we model emails as a list of characters - problems: random string that can contain all sorts of stuff. Email abstraction only allows creation of valid emails.
- This eliminates unnecessary error checking. Less bugs, way to reason about programs more reasonable, no need for checking.
- Type definition: a set of values (mathematical set). Example: type of string is the set of all possible strings.
- Function: is a mapping from one type to another (domain to codomain). We build up entire programs by having only types and functions. For every element in domain there is 1 (and only 1) in the codomain. If you feed a function a value, you get the same value - it is deterministic.
- Example of a function that is not total: map.apply (if you feed the wrong key, exception). This is not a mathematical function. Then you end up with a lie, because type signatures are not telling us everything.

- Algebraic Data Type - 2 types (leaf or primitive types and composite types - List for example)
Products and Sums (Coproducts)
i. Product type: type that is a composition of 2 or more types such as it follows AND combination. Example: Adress has a city and a street. Composite type that has multiple components.
ii. Sum: OR, dual of product type. Example - Either, Option, shapes can be modelled (vs in Java we have to model this through relations), animal.

- Case class is a Product
- Trait, inheritance is a Sum. Enums in Dotty will be a proper Sum type.

Exercise: types.scala
(exercise 1)


## Higher order functions
- Function combinators take a function and return a function.
- State monads are basically just functions. You can build whole programs just composing functions.

Exercise: functions.scala
(exercise 3)


## Higher-Kinded Types
- Type defined as a set of values, for something to have a type means it is an element of that set.
- Types can also be members of sets. Function defined as a mapping from one type to another, but we can also define them as mapping from types to types (for example Int is a type, List is a type constructor and not a type which is a type-level function). Domain is a set of types, and codomain is another set of types. List is a function from a set of types to a set of types. We apply functions, if we feed a type to list we get back a type (List[Int]), but we call it kind instead.

- Kinds can be thought of as "the type of types".
* — The kind of types (the set of all types).
* => * — The kind of type-level functions that accept 1 type (the set of all type-level functions that accept 1 type and return 1 type). The type constructor List has kind * => *, represented as _[_] in Scala. This is the kind of List, Array, Future. Domain and codomain are a set of types.
[*, *] => * — The kind of type-level functions that accept 2 types (the set of all type-level functions that accept 2 types). The type constructor Either has kind [*, *] => *, represented as _[_, _] in Scala. Example is the BoardRow2 defined in the first exercise, Map.

- Kinds are signatures for type-level functions, analog for type constructors.

- Higher-Order Kinds: equivalent of a higher-order function for types. Example: Functor, Traversable (typeclass for this )

// kind annotation: [F[_]] is equivalent to [F: * => *]
// [F[_, _]] is equivalent to [F: [*, *] => *]
trait Traversable[F[_]] {
  def foldLeft[A, Z](fa: F[A], initial: Z)(f: (Z, A) => Z): Z
}
(types.scala exercise 2)

  def gimmeAListUniversal[A]: List[A]  = ???
  //def gimmeAListExistential: List[A] forSome {type A} = 1 :: 2:: Nil
  //gimmeAListExistential.length and there is nothing more we can do with it

- Skolemization

## Type Lambdas
- We can partially apply type constructors.
(types, exercise 5)

## Type classes
- functions exercise 4
dealing with a subset of all possible types and get a set of functionality that we need.

typeclasses.scala
trait Show[A] { //typeclass that accepts 1 type parameter
  def show(a: A): String
}
...

7. Purely functional programs
- Everything is a value
- All functions are deterministic
- All functions are total
- These properties make it possible to test in a clean way
- Everything in our program is an expression and not a side effect

8. Functor
Covariant endofunctor are the usual ones
  trait Functor[F[_]] ...
  trait Apply[F[_]] extends Functor[F[_]]
  trait Applicative[F[_]] extends Apply[F]
