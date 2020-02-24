package amsterdamscala.controller

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import amsterdamscala.repository.OrderRepository

import scala.concurrent._
import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.util.Timeout
import amsterdamscala.domain.Order

import scala.concurrent._

case class OrderApiController(repo: OrderRepository)(implicit system: ActorSystem, executor: ExecutionContext, materializer: Materializer) {

  import spray.json.DefaultJsonProtocol._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import Order._

  val route: Route = concat(
    pathPrefix("orders") {
      concat(
        pathEnd {
          get {
            onSuccess(repo.allOrders()) { orders =>
              complete(orders)
            }
          }
        },
        post {
          entity(as[Order]) { order =>
            onSuccess(repo.saveOrder(order)) { _ =>
              complete((StatusCodes.Created))
            }
          }
        },
        pathPrefix("users") {
          path(IntNumber) { id =>
            get {
              onSuccess(repo.fetchOrder(id)) { order => order.map(complete(_)).getOrElse(complete(StatusCodes.NotFound))
              }
            }
          }
        }
      )
    }
    }
  )


}

