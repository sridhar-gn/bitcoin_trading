package com.test.tookitaki.client

import java.sql.Timestamp

import com.test.tookitaki.response.BitCoinResponse

import scala.concurrent.Future

/**
  * Created by sridhara.g on 9/16/18.
  */
trait BitCoinClient {
  def getBitCointPrice: Future[BitCoinResponse]
}
