package amsterdamscala

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

/**
 * ScalaCheck standalone
 */
class ScalaCheckSpec extends Properties("Order") {
  property("startsWith") = forAll { (a: String, b: String) =>
    (a+b).startsWith(a)
  }
}
