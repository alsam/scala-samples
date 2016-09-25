package spire.samples

import spire.algebra._   // provides algebraic type classes
import spire.math._      // provides functions, types, and type classes
import spire.implicits._ // provides infix operators, instances and conversions

object Sample {

  def quaternion_test = {
    type H = Quaternion[Real]
    val zero = Quaternion.zero[Real]
    val one = Quaternion.one[Real]

    val a = Quaternion(1.0, 2.0, 3.0, 4.0)
    val b = Quaternion(7.0, 8.0, 9.0, 10.0)

    println("quaternion_test: a + b: " + (a + b) + " a * b: " + (a * b))
  }

  // def main(args: Array[String]) {
  //   println("Starting spire.samples.main")

  //   quaternion_test
  // }

}
