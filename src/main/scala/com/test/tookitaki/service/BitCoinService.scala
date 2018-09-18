package com.test.tookitaki.service

import java.sql.Timestamp

import com.test.tookitaki.response.{GetBitCoinMovingAverage, GetBitCoinResponse, GetTradingDecisions}

import scala.concurrent.Future

/**
  * Created by sridhara.g on 9/16/18.
  */
trait BitCoinService {

  /**
    * @param timeStamp accept date to serve the bitcoin price for the day
    * @return returns the bitcoin price for given date*/
  def getBitCoinPrice(timeStamp: Timestamp) : Future[GetBitCoinResponse]

  /**
    * @param fromDate
    * @param toDate
    * @return Moving average for the from_date -> to_date*/
  def getBitCoinMovingAverage(fromDate: Timestamp, toDate: Timestamp): Future[GetBitCoinMovingAverage]

  /**
    * Giving the trading decisions to the end user
    * @return decisions like SELL, BUY or HOLD*/
  def getTradingDecision: Future[GetTradingDecisions]
}
