package com.test.tookitaki.constants

/**
  * Created by sridhara.g on 9/17/18.
  */
object Constants {

  val Window = 10

  object DateConstants {
    val Before: Int = -1

    /*'-' indicates the past days*/
    val pastDaysInNumber: Int = - 52
    val AverageDays = 52
  }

  object TradingConstants {
    val SELL = "SELL"
    val BUY = "BUY"
    val HOLD = "HOLD"
  }

  object BitCoinUrl {
    val url = "https://www.coinbase.com/api/v2/prices/BTC-USD/historic?period=all"
  }
}
