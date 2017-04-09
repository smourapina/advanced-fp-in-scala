## Semigroups

trait Semigroup[A] {
  def append(a1: A, a2: A) : A 
}

typeclasses exercise 3

Monoid: addition, multiplication, events, ...

trait Partitionable...

## Purely functional programs
- Everything is a value
- All functions are deterministic
- All functions are total
- These properties make it possible to test in a clean way
- Everything in our program is an expression and not a side effect

## Functor
Covariant endofunctor are the usual ones
  trait Functor[F[_]] ...
  trait Apply[F[_]] extends Functor[F[_]]
  trait Applicative[F[_]] extends Apply[F]
- Don't use more power than you need (principle in fp programming)
- The more power you provide to the user of a library the less power you have as the implementer of the library