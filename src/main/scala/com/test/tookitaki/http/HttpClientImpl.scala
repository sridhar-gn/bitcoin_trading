package com.test.tookitaki.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCode}
import akka.http.scaladsl.unmarshalling.{FromResponseUnmarshaller, Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.typesafe.config.Config
import org.slf4j.{Logger, LoggerFactory}
import scaldi.{Injectable, Injector}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by sridhara.g on 9/16/18.
  */
class HttpClientImpl (implicit injector: Injector ) extends HttpClient  with Injectable{
  implicit val system: ActorSystem = inject[ActorSystem]
  implicit val fm: ActorMaterializer = inject[ActorMaterializer]
  implicit val ec: ExecutionContext = inject[ExecutionContext]

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  /**@param req is the HttpRequest contails headers, Uri and the HttpMethosType
    *@return Future[T]
  Using Akka http to make rest API's
    * Unmarshal the HttpResponse to type T.*/
  override def execute[T](req: HttpRequest)(implicit um: Unmarshaller[HttpResponse, T]): Future[T] = {
    logger.info(s"HTTP Request = $req")
    Http().singleRequest(req).flatMap{httpResponse =>
      logger.info(s"HTTP Response for the Request = ${httpResponse.entity}")
      Unmarshal(httpResponse).to[T].recover {
        case ex: Exception => {
          logger.error(s"Error in HttpClient -> cause -> ${ex.getCause}  message -> ${ex.getMessage} class -> ${ex.getClass} ${ex.printStackTrace()}")
          throw ex
        }
      }
    }
  }
}
