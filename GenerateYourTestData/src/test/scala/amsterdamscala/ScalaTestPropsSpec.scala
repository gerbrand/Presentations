package amsterdamscala

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

// Inspiration ? https://github.com/circe/circe/blob/1776c0d2d2822bfa282cc674fd7a7c4b8f4cdeda/modules/literal/src/test/scala/io/circe/literal/interpolator/JsonInterpolatorSuite.scala

/**
 * Using ScalaCheckDrivenPropertyChecks from Scalaplus
 */
class ScalaTestPropsSpec  extends AnyFunSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  // TODO add some tests

  // Using for expression to generate data

  // Including dates as well (calendar ?)
  // Maybe, order should be paid within 30 days
  // Also total amount for an order should be less than 30

  // Using arbitrary to generate data for generic types

  // And using combination of above

  // Tweaking generation of data
}
