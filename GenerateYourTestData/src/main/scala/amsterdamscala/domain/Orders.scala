package amsterdamscala.domain

import java.time.ZonedDateTime

import akka.http.scaladsl.model.DateTime
import amsterdamscala.repository.OrderRepository

class Orders(repo: OrderRepository) {
    val allOrders = repo.allOrders

    def saveOrders(date: ZonedDateTime, order: Order) = {

    }

    val getOrder = repo.fetchOrder(_)

}
