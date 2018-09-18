package com.test.tookitaki.http
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshaller

import scala.concurrent.Future
/**
  * Created by sridhara.g on 9/16/18.
  */
trait HttpClient {

  /**@param req HttpRequest
    * @return Future[T]*/
  def execute[T](req: HttpRequest)(implicit um: Unmarshaller[HttpResponse, T]): Future[T]
}
