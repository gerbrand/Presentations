package amsterdamscala.repository

import amsterdamscala.domain.Order

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


class OrderRepository(implicit ec: ExecutionContext)  {
  var orderMap: mutable.HashMap[Int, Order] = new mutable.HashMap[Int, Order]()

  def fetchOrder(orderId: Int) = {
   Future.successful(orderMap.get(orderId))
  }

  def saveOrder(order: Order) = {
    orderMap.put(order.id, order) match {
      case Some(order) => Future.failed(new RuntimeException(s"Order with ${order.id} already exists"))
      case _ => Future.successful(s"Saved order ${order.id}")
    }
  }

  def allOrders() = Future.successful(orderMap.values)
}

class DuplicateException extends RuntimeException

