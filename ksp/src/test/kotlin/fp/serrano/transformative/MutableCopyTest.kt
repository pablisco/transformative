package fp.serrano.transformative

import org.junit.jupiter.api.Test

class MutableCopyTest {

  @Test
  fun `mutate one property`() {
    """
      |data class Person(val name: String, val age: Int)
      |
      |val p1 = Person("Alex", 1)
      |val p2 = p1.copy { age = age + 1 }
      |val r = p2.age
      """.evals("r" to 2)
  }

  @Test
  fun `mutate two properties`() {
    """
      |data class Person(val name: String, val age: Int)
      |
      |val p1 = Person("Alex", 1)
      |val p2 = p1.copy { 
      |  name = name + " Serrano"
      |  age = age + 1 
      |}
      |val r = p2.age
      |val n = p2.name
      """.evals("r" to 2, "n" to "Alex Serrano")
  }

  @Test
  fun `mutate two properties (advanced)`() {
    """
      |data class Person(val name: String, val age: Int)
      |
      |val p1 = Person("Alex", 1)
      |val p2 = p1.copy { 
      |  name = "${"\$"}name Serrano"
      |  age++ 
      |}
      |val r = p2.age
      |val n = p2.name
      """.evals("r" to 2, "n" to "Alex Serrano")
  }

  @Test
  fun `fails on non-data class`() {
    """
      |class Person(val name: String, val age: Int)
      |
      |val p1 = Person("Alex", 1)
      |val p2 = p1.copy { }
      """.failsWith { it.contains("Unresolved reference: copy") }
  }

  @Test
  fun `empty transform does nothing`() {
    """
      |data class Person(val name: String, val age: Int)
      |
      |val p1 = Person("Alex", 1)
      |val p2 = p1.copy { }
      |val r = p2.age
      """.evals("r" to 1)
  }

  @Test
  fun `works on generic classes`() {
    """
      |data class Person<A>(val name: String, val age: A)
      |
      |val p1: Person<Int> = Person("Alex", 1)
      |val p2 = p1.copy { age = age + 1 }
      |val r = p2.age
      """.evals("r" to 2)
  }

  @Test
  fun `each for lists`() {
    """
      |data class Person<A>(val name: String, val age: Int, val anns: List<A>)
      |
      |val p1: Person<Int> = Person("Alex", 1, listOf(1, 10))
      |val p2 = p1.copy { anns = anns.map { it + 1 } }
      |val r = p2.anns.first()
      """.evals("r" to 2)
  }

  @Test
  fun `each for maps`() {
    """
      |data class Person(val name: String, val age: Int, val things: Map<String, Int>)
      |
      |val p1: Person = Person("Alex", 1, mapOf("chair" to 1, "pencil" to 10))
      |val p2 = p1.copy { things = things.mapValues { it.value + 1 } }
      |val r = p2.things["chair"]
      """.evals("r" to 2)
  }
}
