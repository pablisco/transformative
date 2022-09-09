package fp.serrano.transformative

import org.junit.jupiter.api.Test

class TransformFunctionTest {

  @Test
  fun `simple test`() {
    """
      |data class Person(val name: String, val age: Int)
      |
      |val p1 = Person("Alex", 1)
      |val p2 = p1.transform(age = { it + 1 })
      |val r = p2.age
      """.evals("r" to 2)
  }

  @Test
  fun `fails on non-data class`() {
    """
      |class Person(val name: String, val age: Int)
      |
      |val p1 = Person("Alex", 1)
      |val p2 = p1.transform(age = { it + 1 })
      """.failsWith { it.contains("Unresolved reference: transform") }
  }

  @Test
  fun `empty transform does nothing`() {
    """
      |data class Person(val name: String, val age: Int)
      |
      |val p1 = Person("Alex", 1)
      |val p2 = p1.transform()
      |val r = p2.age
      """.evals("r" to 1)
  }

  @Test
  fun `works on generic classes`() {
    """
      |data class Person<A>(val name: String, val age: A)
      |
      |val p1: Person<Int> = Person("Alex", 1)
      |val p2 = p1.transform(age = { it + 1 })
      |val r = p2.age
      """.evals("r" to 2)
  }

  @Test
  fun `each for lists`() {
    """
      |data class Person<A>(val name: String, val age: Int, val anns: List<A>)
      |
      |val p1: Person<Int> = Person("Alex", 1, listOf(1, 10))
      |val p2 = p1.transform(annsEach = { it + 1 })
      |val r = p2.anns.first()
      """.evals("r" to 2)
  }

  @Test
  fun `each for maps`() {
    """
      |data class Person(val name: String, val age: Int, val things: Map<String, Int>)
      |
      |val p1: Person = Person("Alex", 1, mapOf("chair" to 1, "pencil" to 10))
      |val p2 = p1.transform(thingsEach = { it.value + 1 })
      |val r = p2.things["chair"]
      """.evals("r" to 2)
  }
}
