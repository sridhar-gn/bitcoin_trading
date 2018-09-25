package com.test.tookitaki.service

import java.sql.Timestamp

import com.test.tookitaki.client.BitCoinClient
import com.test.tookitaki.constants.Constants
import com.test.tookitaki.exceptions.BitcoinPriceNotFoundException
import com.test.tookitaki.mapper.BitCoinMapper
import com.test.tookitaki.protocol.BitCoinProtocol
import com.test.tookitaki.response.{GetBitCoinMovingAverage, GetBitCoinResponse, GetTradingDecisions, PriceDetails}
import com.test.tookitaki.trading.TradingClient
import com.test.tookitaki.utils.{DateUtils, PriceFormatter}
import org.slf4j.{Logger, LoggerFactory}
import scaldi.{Injectable, Injector}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by sridhara.g on 9/16/18.
  */
class BitCoinServiceImpl(implicit injector: Injector) extends BitCoinService with
  BitCoinProtocol with Injectable with BitCoinMapper {

  val bitCoinClient: BitCoinClient = inject[BitCoinClient]
  implicit val ec: ExecutionContext = inject[ExecutionContext]
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  val tradingClient = inject[TradingClient]

  /*Get the Bit coin price for the given date*/
  override def getBitCoinPrice(timeStamp: Timestamp): Future[GetBitCoinResponse] = {
    bitCoinClient.getBitCointPrice.map(resp => {
      val result = toPriceDetailsEntity(resp).filter(_.time.getTime == timeStamp.getTime)
      /*throw exception if price details is empty for the given date*/
      if(result.isEmpty) throw new BitcoinPriceNotFoundException
      GetBitCoinResponse(bitCoinPrice = result)
    })
  }


  /*Get the average price for the from_date and to_date*/
  override def getBitCoinMovingAverage(fromDate: Timestamp, toDate: Timestamp): Future[GetBitCoinMovingAverage] = {
    val a = getBuckets(10, fromDate, toDate)
    bitCoinClient.getBitCointPrice.map(response => {
      val days = DateUtils.getDifferenceBetweenDates(fromDate = fromDate, toDate = toDate)
      val filterPrices =  toPriceDetailsEntity(response).filter(d => d.time.getTime >= fromDate.getTime && d.time.getTime <= toDate.getTime)
     val movingAverage = filterPrices.map(_.price.toFloat).sum / days
      GetBitCoinMovingAverage(movingPrice = PriceFormatter.format(movingAverage))
    })
  }

  /*Get the trading decisions*/
  override def getTradingDecision: Future[GetTradingDecisions] = {
    val yesterdayTimeStamp = DateUtils.getTimestampByAddingDays(Constants.DateConstants.Before)

    for {
      bitCoinResponeF <- bitCoinClient.getBitCointPrice

      movingAveragePriceDetailsF <-
      /*Calculation the moving average historic price i,e(considering previous 52 days)*/
      Future {
        toPriceDetailsEntity(bitCoinResponeF)
      }

      transform <- {
        /*This is to get the yesterday timeStamp*/
        logger.info(s"yesterdayTimeStamp = $yesterdayTimeStamp")

        /*Getting the timestamp by passing the past days*/

        val previousWeekTimestamp = DateUtils.getTimestampByAddingDays(days = Constants.DateConstants.pastDaysInNumber)
        logger.info(s"previousWeekTimestamp $previousWeekTimestamp")

       val movingAveragePriceDetails =  movingAveragePriceDetailsF.filter(d => d.time.getTime <= yesterdayTimeStamp &&
          d.time.getTime >= previousWeekTimestamp)

        Future {
          val currentPriceOpt = movingAveragePriceDetailsF.find(d => d.time.getTime == DateUtils.getDateFromTimestamp(DateUtils.Now).getTime)

          /*Calculation the moving average (sum(previous price) / previous number of days)*/
          val movingAverage = movingAveragePriceDetails.map(_.price.toFloat).sum / Constants.DateConstants.AverageDays


         val tradingDecision =  currentPriceOpt match {
            case Some(currentPriceDetails) =>
              /*Get the trading decisions whether user to SELL,BUY or HOLD*/
              tradingClient.getTradingDecision(currentPrice = currentPriceDetails.price.toFloat,
              movingAverage = movingAverage)
            case None => {
              logger.info(s"current price not found")
              (Constants.TradingConstants.HOLD, movingAverage)
            }
          }
          /*return the entity*/
          GetTradingDecisions(decision = tradingDecision._1, currentPrice = tradingDecision._2, movingAveragePrice = f"$movingAverage%1.2f".toFloat)
        }
      }
    } yield transform


  }

  /**@param window
    * @param startDate
    * @param endDate*/
  private def getPriceDetailsBuckets(window: Int, startDate: Timestamp, endDate: Timestamp) = {
    bitCoinClient.getBitCointPrice.map(response => {
     val priceDetailsInTimeRange =  toPriceDetailsEntity(response).filter(r => r.time.getTime >= startDate.getTime &&
      r.time.getTime <= endDate.getTime)
      partition(priceDetailsInTimeRange, window)

    })
  }


  private def partition(priceDetails: Seq[PriceDetails], window: Int) = {
    /*split the array for the given window */
   val values =  priceDetails.sliding(window, window).toList
    values.map(pd => pd.max.price.toFloat)
  }
}
