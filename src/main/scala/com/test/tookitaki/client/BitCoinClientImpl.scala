package com.test.tookitaki.client

import java.sql.Timestamp

import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import com.test.tookitaki.constants.Constants
import com.test.tookitaki.http.HttpClient
import com.test.tookitaki.protocol.BitCoinProtocol
import com.test.tookitaki.response.BitCoinResponse
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}
import scaldi.{Injectable, Injector}
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

/**
  * Created by sridhara.g on 9/16/18.
  */
class BitCoinClientImpl(implicit injector: Injector) extends BitCoinClient with
   BitCoinProtocol with Injectable {

  val httpClient: HttpClient = inject[HttpClient]
  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  /*Get the historic data by calling the bit coin url
  * map bit coin response to domain response */
  override def getBitCointPrice: Future[BitCoinResponse] = {
    val httpRequest = HttpRequest(method = HttpMethods.GET,
      uri = Constants.BitCoinUrl.url)
    httpClient.execute[BitCoinResponse](httpRequest)
  }
}
