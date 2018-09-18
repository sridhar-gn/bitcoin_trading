package com.test.tookitaki.response

/**
  * Created by sridhara.g on 9/16/18.
  */


case class Price(price: String, time: String)
case class Data(prices: Seq[Price])
case class BitCoinResponse(data: Data)
