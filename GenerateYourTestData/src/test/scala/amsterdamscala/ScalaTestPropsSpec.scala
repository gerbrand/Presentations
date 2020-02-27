package amsterdamscala

import java.time.{ZoneId, ZonedDateTime}

import amsterdamscala.domain.{CountryCode, Currency, Email, Money, Name, Percentage, Price}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

// Inspiration ? https://github.com/circe/circe/blob/1776c0d2d2822bfa282cc674fd7a7c4b8f4cdeda/modules/literal/src/test/scala/io/circe/literal/interpolator/JsonInterpolatorSuite.scala

/**
 * Using ScalaCheckDrivenPropertyChecks from Scalaplus
 */
class ScalaTestPropsSpec extends AnyWordSpecLike with Matchers with ScalaCheckDrivenPropertyChecks with TableDrivenPropertyChecks {

  "CountryCode" should {

    "parse String" in {
      forAll(
        Table(
          ("input", "expected"),
          ("NL", CountryCode.NL),
          ("BE", CountryCode.BE),
          ("nl", CountryCode.NL),
        )
      ) { (input, expected) => {
        assert(CountryCode.parse(input) === expected)
      }
      }
    }
  }

  val emailGen = for {
    username <- Gen.asciiPrintableStr
    server   <- Gen.asciiPrintableStr
  } yield (Email(s"$username@$server"))
  import scala.jdk.CollectionConverters._

  val posBigDecimal = Gen.choose(0, 100000d).map(BigDecimal(_))
  val zoneGen = Gen.oneOf(ZoneId.getAvailableZoneIds.asScala).map(ZoneId.of(_))
  val dateTimeGen   = Gen.calendar.map(c => ZonedDateTime.ofInstant(c.toInstant, ZoneId.systemDefault()))



  val percentage = Gen.choose(0, 1).map(Percentage(_))
  val currencyGen = Gen.oneOf(Seq(Currency.Euro, Currency.Chf))

  val priceGen = for {
    currency <- currencyGen
    withoutTax <- posBigDecimal.map(Money(_, currency))
    vatRate    <- percentage
    withTax = Money(withoutTax.value * (1 + vatRate.value), withoutTax.currency)
  } yield Price(withoutTax, withTax, vatRate)

  "price" should {
    "have right tax" in {
      forAll(priceGen) {
        case price: Price => {
          whenever(price.vatRate.value>0) {
            assert(price.withoutTax.value < price.withTax.value)
            price.withoutTax should be (price.withTax.value -  (price.withTax.value * price.vatRate.value))
          }
          whenever(price.vatRate.value==0) { price.withoutTax should be (price.withTax.value) }
        }
      }
    }
  }

  implicit val arbitraryEmail         = Arbitrary(emailGen)
  implicit val arbitraryCurrency      = Arbitrary(currencyGen)
  implicit val arbitraryDateTime      = Arbitrary(dateTimeGen)
  implicit val arbitraryPriceGen      = Arbitrary(priceGen)

  import org.scalacheck.ScalacheckShapeless._

//  implicitly[Arbitrary[Name]]

  "name" should {
    "should contain surname" in {
      forAll { n: Name =>
        whenever(n.surName.value.length > 0) {
          n.display() should include(s" ${n.surName.value}")
        }
        whenever(n.surName.value.isEmpty) {
          // Contains bug
          n.display() should be(n.foreName.value)
        }
      }
    }
  }

}
