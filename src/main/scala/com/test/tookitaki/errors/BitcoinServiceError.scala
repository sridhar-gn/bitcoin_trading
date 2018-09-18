package com.test.tookitaki.errors

import akka.http.scaladsl.model.StatusCode
import spray.json._
import spray.json.DefaultJsonProtocol._
/**
  * Created by sridhara.g on 9/18/18.
  */


object BitcoinServiceError {
  implicit val jsonWriter = new RootJsonWriter[BitcoinServiceError] {
    override def write(obj: BitcoinServiceError): JsValue = {
      JsObject("status" -> JsNumber(obj.statusCode.intValue),
        "code" -> JsString(obj.code),
        "message" -> JsString(obj.message))
    }

  }
}

case class BitcoinServiceError(val statusCode: StatusCode,
                               val code : String,
                               val message: String) {

  def toJsonString: String = this.toJson.toString()
}
