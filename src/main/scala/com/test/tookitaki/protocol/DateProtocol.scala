package com.test.tookitaki.protocol

import java.sql.Timestamp

import spray.json.{JsNumber, JsValue, RootJsonFormat}

/**
  * Created by sridhara.g on 9/16/18.
  */
trait DateProtocol {

  implicit val timestampJF = new RootJsonFormat[Timestamp] {

    override def write(obj: Timestamp): JsValue = {
      JsNumber(obj.getTime)
    }

    override def read(json: JsValue): Timestamp = {
      val value = json.asInstanceOf[JsNumber].value
      new Timestamp(value.toLong)
    }
  }

}

object DateProtocol extends DateProtocol
