package com.test.tookitaki.protocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.test.tookitaki.response._
import spray.json.{DefaultJsonProtocol, JsNumber, JsObject, JsString, JsValue, RootJsonWriter}

/**
  * Created by sridhara.g on 9/16/18.
  */
trait BitCoinProtocol extends DefaultJsonProtocol with SprayJsonSupport with
  DateProtocol {
  implicit val priceDetailsJF = jsonFormat2(PriceDetails.apply)
  implicit val priceJF = jsonFormat2(Price.apply)
  implicit val dataJF = jsonFormat1(Data.apply)
  implicit val bitCoinResponseJF = jsonFormat1(BitCoinResponse.apply)
  implicit val getBitCoinResponseJF = jsonFormat(GetBitCoinResponse.apply _, "bitcoin_price")
  implicit val getBitCoinAveragePriceJF = jsonFormat(GetBitCoinMovingAverage.apply _, "moving_price")


}


