package fks4j.kafka.streams.topology;

import org.junit.jupiter.api.Test;

class StreamBuilderTest {

  // left identity
  // monad.unit(X).flatMap(f) = f(X)

  // right identity
  // x.flatMap( y => Monad.unit(Y)) == x
  // monad.unit(X).flatMap( y => Monad.unit(Y)) == Monad(x)

  // associativity
  // x.flatMap(f).flatMap(g) = o.flatMap(x => f(x).flatMap(g))
  // monad.unit(x(.flatMap(f).flatMap(g) =   f(x).flatMap(g))
  @Test
  void environment() {
  }

  @Test
  void map() {
  }

  @Test
  void get() {
  }

  @Test
  void flatMap() {
  }

  @Test
  void andThen() {
  }

  @Test
  void runS() {
  }
}