package amsterdamscala

import java.time.{ZoneId, ZonedDateTime}

import amsterdamscala.domain._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

/**
 * Using ScalaCheckDrivenPropertyChecks from Scalaplus
 *
 * Some of the test fails. Up to the reader to fix them.
 */
class ScalaTestPropsSpec extends AnyWordSpecLike with Matchers with ScalaCheckDrivenPropertyChecks with TableDrivenPropertyChecks {

  // Change minSize set to a high number (like 100000) and run the test again.
  // Some tests will run longer. Failing test will fail just as fast.
  implicit override val generatorDrivenConfig = PropertyCheckConfiguration(minSize = 20)

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

  val percentageGen = Gen.choose(0, 1).map(Percentage(_))
  val currencyGen = Gen.oneOf(Seq(Currency.Euro, Currency.Chf))

  val priceGen = for {
    currency <- currencyGen
    withoutTax <- posBigDecimal.map(Money(_, currency))
    vatRate    <- percentageGen
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

  val amountGen = for {
    n0 <- Gen.posNum[Int]
    n1 <- Gen.posNum[Int]
  } yield Amount(BigDecimal(n0, n1))

  val moneyGen = for {
    amount <- Gen.chooseNum(0, 1000000)
    currency <- currencyGen
  } yield Money(amount, currency)

  implicit val arbitraryEmail = Arbitrary(emailGen)
  implicit val arbitraryCurrency = Arbitrary(currencyGen)
  implicit val arbitraryDateTime = Arbitrary(dateTimeGen)
  implicit val arbitraryPrice = Arbitrary(priceGen)
  implicit val arbitraryMoney = Arbitrary(moneyGen)
  implicit val arbitraryPercentage = Arbitrary(percentageGen)
  implicit val arbitraryAmount = Arbitrary(amountGen)

  import org.scalacheck.ScalacheckShapeless._

//  implicitly[Arbitrary[Name]]

  "name" should {
    "contain surname" in {
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

  val userGen = for {
    id <- arbitrary[Int]
    name <- Gen.resultOf(Name)
    email <- emailGen
  } yield (User(id, name, email))

  "users" should {
    "be generated" in {
      forAll(userGen) { user => {
        user.email.value should not be(empty)
      }}
    }
  }


  "orders" should {
    "have correct amount" in {
      forAll { orders:List[Order] => {
        whenever(! orders.isEmpty) {
          assert(Order.totalAmount(orders) >= 0)
        }
      }}
    }
  }

}
