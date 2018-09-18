package com.test.tookitaki.controller

import java.sql.Timestamp

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.test.tookitaki.protocol.{BitCoinProtocol, DateProtocol}
import com.test.tookitaki.service.BitCoinService
import scaldi.{Injectable, Injector}

/**
  * Created by sridhara.g on 9/16/18.
  */
class BitCoinController(implicit val injector : Injector) extends
   BitCoinProtocol with Injectable with BaseController with DateProtocol {

  val bitCoinService: BitCoinService = inject[BitCoinService]
  val route: Route = path(Prefix) {
    get {
      parameter('date.as[Timestamp]) { date =>
        val f = bitCoinService.getBitCoinPrice(date)
        onSuccess(f) {
          resp => complete(resp)
        }
      }
    }
  } ~ path(Prefix / "moving" / "average") {
    get {
      parameter('from_date.as[Timestamp], 'to_date.as[Timestamp]) { (fromDate, toDate) => {
        val f = bitCoinService.getBitCoinMovingAverage(fromDate = fromDate, toDate = toDate)
        onSuccess(f) { resp =>
          complete(resp)
        }
      }
      }
    }
  } ~path(Prefix / "trading") {
    get {
      val f = bitCoinService.getTradingDecision
      onSuccess(f) { resp =>
        complete(resp)
      }
    }
  }
}
