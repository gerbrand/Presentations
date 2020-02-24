package amsterdamscala

import java.time.OffsetDateTime

import spray.json.{DefaultJsonProtocol, JsObject, JsValue, JsonFormat, RootJsonFormat}
import spray.json._
import DefaultJsonProtocol._
import amsterdamscala.domain.Good

package object domain {

  case class Order(id: Int, email: Email, discount: BigDecimal, lines: Iterable[OrderLine])

  case class Email(email: String) extends AnyVal

  object Email {
    implicit val format: RootJsonFormat[Email] = new RootJsonFormat[Email] {
      override def write(obj: Email): JsValue = obj.email.toJson

      override def read(json: JsValue): Email = Email(json.convertTo)
    }
  }

  case class OrderLine(description: String, good: Good, amount: BigDecimal)

  object OrderLine {
    implicit val format: RootJsonFormat[OrderLine] = jsonFormat3(OrderLine.apply)
  }

  case class Good(price: BigDecimal, article: String, goodType: GoodType)

  object Good {
    implicit val format: RootJsonFormat[Good] = new RootJsonFormat[Good] {
      // deserialization code
      override def read(json: JsValue): Good = {
        val fields = json.asJsObject().fields
        Good(
          goodType = {
            val k = fields("type").convertTo[String]
            GoodType.goodTypes.filter(g => g.key == k).head
          },
          price = fields("price").convertTo[BigDecimal],
          article = fields("article").convertTo[String]
        )
      }

      // serialization code
      override def write(good: Good): JsValue = JsObject(
        "type" -> good.goodType.key.toJson,
        "price" -> good.price.toJson,
        "article" -> good.article.toJson
      )
    }
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
    val vat = 0.20

    def totalAmount(orders: Iterable[Order]): BigDecimal = orders.map(totalAmount).sum

    def totalAmount(order: Order): BigDecimal = order.lines.map(l => l.amount * l.good.price).sum

    def totalGrossAmount(order: Order): BigDecimal = totalAmount(order) * (1 - vat)

    implicit val format: RootJsonFormat[Order] = jsonFormat4(Order.apply)


  }

}
