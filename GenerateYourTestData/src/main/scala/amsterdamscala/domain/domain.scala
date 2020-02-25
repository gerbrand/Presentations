package amsterdamscala

import java.time.ZonedDateTime

import io.circe.{Decoder, Encoder}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

package object domain {

  case class Order(id: Int, date: ZonedDateTime, email: Email, discount: BigDecimal, lines: Iterable[OrderLine])

  case class Email(email: String) extends AnyVal

  object Email {
    object JsonCodec {
      implicit val decoder: Decoder[Email] =
        Decoder.decodeString
          .emap(email => Right(Email.apply(email)))

      implicit val encoder: Encoder[Email] =
        Encoder.encodeString.contramap(v => v.email)
    }
  }

  case class OrderLine(description: String, good: Good, amount: BigDecimal)

  case class Good(price: BigDecimal, article: String, goodType: GoodType)

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
    val vat = 0.20

    def totalAmount(orders: Iterable[Order]): BigDecimal = orders.map(totalAmount).sum

    def totalAmount(order: Order): BigDecimal = order.lines.map(l => l.amount * l.good.price).sum

    def totalGrossAmount(order: Order): BigDecimal = totalAmount(order) * (1 - vat)

  }



}
