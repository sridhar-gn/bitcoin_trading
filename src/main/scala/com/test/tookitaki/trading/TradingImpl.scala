package com.test.tookitaki.trading

import com.test.tookitaki.constants.Constants
import org.slf4j.LoggerFactory

/**
  * Created by sridhara.g on 9/18/18.
  */


class TradingImpl extends TradingClient{
  val logger = LoggerFactory.getLogger(this.getClass)

  /*Obtain the trading decisions based on:
        * SELL = if the current price value greater than the moving average
        * BUY = if the current price value less of equal to the moving average
        * HOLD = if current value matches the moving average */
  override def getTradingDecision(currentPrice: Float, movingAverage: Float): (String, Float) = {
    if (currentPrice < movingAverage) {
      logger.info(s"current price is less ${currentPrice}")
      (Constants.TradingConstants.BUY, currentPrice)

    } else if (currentPrice > movingAverage) {
      logger.info(s"moving average price is more $movingAverage")
      (Constants.TradingConstants.SELL, currentPrice)
    } else {
      logger.info(s"Hold the price")
      (Constants.TradingConstants.HOLD, currentPrice)
    }
  }
}
