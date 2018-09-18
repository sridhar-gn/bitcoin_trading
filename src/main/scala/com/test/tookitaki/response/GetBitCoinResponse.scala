package com.test.tookitaki.response

import java.sql.Timestamp

import com.test.tookitaki.protocol.DateProtocol._
import com.test.tookitaki.utils.DateUtils
import spray.json.{DefaultJsonProtocol, JsValue, RootJsonFormat}

/**
  * Created by sridhara.g on 9/16/18.
  */


case class PriceDetails(price: String, time: Timestamp)
case class GetBitCoinResponse(bitCoinPrice: Seq[PriceDetails])



