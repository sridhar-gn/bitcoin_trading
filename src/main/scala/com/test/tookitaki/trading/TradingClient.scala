package com.test.tookitaki.trading

/**
  * Created by sridhara.g on 9/18/18.
  */

trait TradingClient {
  /**
    * @param currentPrice
    * @param movingAverage
    * @return trading decison and the price*/
def getTradingDecision(currentPrice: Float, movingAverage: Float): (String, Float)

}
