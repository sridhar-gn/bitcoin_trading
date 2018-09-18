package com.test.tookitaki.handlers

import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.{HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.ExceptionHandler
import com.test.tookitaki.errors.BitcoinServiceError
import com.test.tookitaki.exceptions.BitcoinPriceNotFoundException

/**
  * Created by sridhara.g on 9/16/18.
  */
trait BitcoinExceptionHandler  {

  implicit val  bitcoinExceptionHandler = ExceptionHandler {
    case ex: Exception => {
      val pf = domainException orElse defaultException
      val error = pf(ex)
      complete(error.statusCode, HttpEntity(`application/json`, error.toJsonString))
    }
  }

  def defaultException: PartialFunction[Exception, BitcoinServiceError] = {
    case ex: Exception => {
       BitcoinServiceError(StatusCodes.InternalServerError, "internal_service_error",
        "Internal service error")
    }
  }
  def domainException: PartialFunction[Exception, BitcoinServiceError] = {
    case ex: BitcoinPriceNotFoundException => {
      BitcoinServiceError(StatusCodes.NotFound, "not_found",
        "Bitcoin price not found")
    }
  }
}
