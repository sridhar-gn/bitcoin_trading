package com.test.tookitaki.response

import spray.json.DefaultJsonProtocol._
import spray.json.{DefaultJsonProtocol, JsNull, JsNumber, JsObject, JsString, JsValue, JsonFormat, RootJsonWriter, deserializationError}

/**
  * Created by sridhara.g on 9/17/18.
  */
case class GetTradingDecisions(decision: String, currentPrice: Float,
                               movingAveragePrice: Float, currency: String = "USD")

object GetTradingDecisions  {

  /*converting float values to 2 decimal places*/
  implicit object FloatJsonFormat extends JsonFormat[Float] {
    def write(x: Float) = JsNumber(f"$x%1.2f")
    def read(value: JsValue) = value match {
      case JsNumber(x) => x.floatValue
      case JsString(str) => {
        str.toFloat
      }
      case JsNull   => Float.NaN
      case x => deserializationError("Expected Float as JsNumber, but got " + x)
    }
  }

  implicit val getTradingDecisionsJF = jsonFormat(GetTradingDecisions.apply _, "decision", "current_price",
  "moving_average", "currency")
}
