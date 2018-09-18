package com.test.tookitaki

import java.sql.Timestamp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import com.test.tookitaki.service.BitCoinServiceImpl
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncFunSpec, FunSpec, Matchers}
import akka.stream.ActorMaterializer
import com.test.tookitaki.client.{BitCoinClient, BitCoinClientImpl}
import com.test.tookitaki.constants.Constants
import com.test.tookitaki.http.{HttpClient, HttpClientImpl}
import com.test.tookitaki.mapper.BitCoinMapper
import com.test.tookitaki.protocol.BitCoinProtocol
import com.test.tookitaki.response._
import com.test.tookitaki.trading.{TradingClient, TradingImpl}
import com.test.tookitaki.utils.PriceFormatter
import scaldi.Module

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by sridhara.g on 9/17/18.
  */
class BitCoinSpec extends AsyncFunSpec with AsyncMockFactory with Matchers
  with BitCoinProtocol with BitCoinMapper {
  val bitCoinClient: BitCoinClient = mock[BitCoinClient]

  implicit val injector = new Module {
    bind[ExecutionContext] to scala.concurrent.ExecutionContext.global
    bind[ActorSystem] to ActorSystem("test") destroyWith (_.terminate())
    bind[ActorMaterializer] to ActorMaterializer()(inject[ActorSystem])
    bind[BitCoinClient] to new BitCoinClientImpl()
    bind[HttpClient] to new HttpClientImpl()
    bind[TradingClient] to new TradingImpl
  }

  val bitCoinService = new BitCoinServiceImpl()

  describe("Bitcoin Trading") {
    describe(".getBitCoinPrice") {
      it("should return bit coin price") {
        val price = PriceDetails(price = "6251.96", new Timestamp(1537209000000l))
        val expectedResult = GetBitCoinResponse(Seq(price))

        /**
          * @param timestamp =  September 17, 2018*/
        bitCoinService.getBitCoinPrice(timeStamp = new Timestamp(1537209000000l)).flatMap { actualResult => {
          assertResult(expectedResult)(actualResult)
          actualResult.bitCoinPrice.head.price.toFloat shouldEqual expectedResult.bitCoinPrice.head.price.toFloat

        }
        }
      }
    }

    describe(".movingAverage") {

      import akka.http.scaladsl.unmarshalling.Unmarshal
      implicit val system = ActorSystem("http-client")
      implicit val materializer = ActorMaterializer()

      val apiUri = Constants.BitCoinUrl.url
      val request = HttpRequest(method = HttpMethods.GET, uri = Uri(apiUri))
      /*September 9, 2018*/
      val toDate = new Timestamp(1536517800000l)

      val movingPrice = 6827.8535

      /*August 9, 2018 */
      val fromDate = new Timestamp(1533839400000l)

      /* calculation the days: from_date -> to_date*/
      val days = (1536517800000L - 1533839400000L) / (1000 * 60 * 60 * 24)
      it("should return bit coin moving average") {


        val expectedResult = for {
          response <- Http().singleRequest(request)
          transform <- Unmarshal(response.entity).to[BitCoinResponse].map(resp => {
            val filterPrices = toPriceDetailsEntity(resp).filter(d => d.time.getTime >= fromDate.getTime &&
              d.time.getTime <= toDate.getTime)
            val movingAverage = filterPrices.map(_.price.toFloat).sum / days
            GetBitCoinMovingAverage(movingPrice = PriceFormatter.format(movingAverage) )
          })
        } yield transform

        bitCoinService.getBitCoinMovingAverage(fromDate = fromDate, toDate = toDate).flatMap(actualResult => {
          expectedResult.map(movingAverage => {
            assertResult(movingAverage)(actualResult)
            movingAverage shouldEqual actualResult
          })
        })
      }
    }

    describe(".trading") {


      /*September 9, 2018*/
      val toDate = new Timestamp(1536517800000l)


      /*August 9, 2018 */
      val fromDate = new Timestamp(1533839400000l)

      /*
      * calculation the days: from_date -> to_date*/
      val days = (1536517800000L - 1533839400000L) / (1000 * 60 * 60 * 24)


      it ("trading decisions") {
        import akka.http.scaladsl.unmarshalling.Unmarshal
        implicit val system = ActorSystem("http-client")
        implicit val materializer = ActorMaterializer()

        val apiUri = Constants.BitCoinUrl.url
        val request = HttpRequest(method = HttpMethods.GET, uri = Uri(apiUri))

        val expectedResult = for {
          response <- Http().singleRequest(request)
          content <- Unmarshal(response.entity).to[BitCoinResponse].map(resp => {
            val priceDetails = toPriceDetailsEntity(resp)
            /*
            * Assuming everyday has bitcoin price*/
            val movingPrice = priceDetails.slice(1, Constants.DateConstants.AverageDays + 1).
              map(_.price.toFloat).sum / Constants.DateConstants.AverageDays

            val currentPrice = priceDetails.take(1).head
            val decison = {
              if(currentPrice.price.toFloat > movingPrice) {
                Constants.TradingConstants.SELL
              } else if (currentPrice.price.toFloat < movingPrice){
                Constants.TradingConstants.BUY
              } else {
                Constants.TradingConstants.HOLD
              }
            }
            GetTradingDecisions(decision = decison, currentPrice = currentPrice.price.toFloat,
              movingAveragePrice = movingPrice)
          })
        } yield content

        bitCoinService.getTradingDecision.flatMap(actualResult => {
          expectedResult.map(eResult => {
            assertResult(eResult)(actualResult)
            actualResult shouldEqual eResult
          })
        })
      }
    }
  }
}
