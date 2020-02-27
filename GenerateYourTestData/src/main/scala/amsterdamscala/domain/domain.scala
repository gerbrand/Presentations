package amsterdamscala

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

package object domain {
  case class Email(email: String) extends AnyVal

  case class ForeName(value: String) extends AnyVal
  case class SurName(value: String) extends AnyVal
  case class Name(foreName: ForeName, surName: SurName) {
    def display() = s"${foreName.value} ${surName.value}"
  }

  case class User(name: Name, email: Email)

  case class Percentage(value: BigDecimal) extends AnyVal

  case class Amount(value: BigDecimal) extends AnyVal

  case class Price(withoutTax: Money, withTax: Money, vatRate: Percentage)

  case class CountryCode(code: String) extends AnyVal
  object CountryCode {
    val BE = CountryCode("BE")
    val NL = CountryCode("NL")
    val DE = CountryCode("DE")
    val FA = CountryCode("FA")
    val UK = CountryCode("UK")
    val GB = UK

    def parse(v: String):CountryCode  = {
      require(v.matches("^[a-zA-Z][a-zA-Z]$"))
      new CountryCode(v.toUpperCase())
    }
  }
  case class Order(id: Int, date: ZonedDateTime, user:User, discount: Percentage, lines: Iterable[OrderLine], country: CountryCode)

  object Email {
    object JsonCodec {
      implicit val decoder: Decoder[Email] =
        Decoder.decodeString
          .emap(email => Right(Email.apply(email)))

      implicit val encoder: Encoder[Email] =
        Encoder.encodeString.contramap(v => v.email)
    }
  }

  case class OrderLine(description: String, good: Good, amount: Amount)

  case class Currency(currencyCode: String) extends AnyVal
  case class Money(value: BigDecimal, currency: Currency)
  case class Good(price: Money, article: String, goodType: GoodType)

  object Currency {
    val Euro = Currency("EUR")
    val Chf = Currency("Chf")
  }
  sealed trait GoodType {
    def key: String
  }

  object GoodType {

    case object Food extends GoodType {
      val key = "food"
    }

    case object Electronics extends GoodType {
      val key = "electronics"
    }

    case object Beverage extends GoodType {
      val key = "beverage"
    }

    val goodTypes = Vector(Food, Electronics, Beverage)
  }


  object Order {
    val vat = Percentage(0.20)

    def totalAmount(orders: Iterable[Order]): BigDecimal = orders.map(totalAmount).sum

    def totalAmount(order: Order): BigDecimal = order.lines.map(l => l.amount.value * l.good.price.value).sum

    def totalGrossAmount(order: Order): BigDecimal = totalAmount(order) * (1 - vat.value)

  }



}
