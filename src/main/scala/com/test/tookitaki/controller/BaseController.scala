package com.test.tookitaki.controller
import akka.http.scaladsl.server.Directives._

/**
  * Created by sridhara.g on 9/16/18.
  */
trait BaseController {
  val Prefix = "api" / "v1" / "bitcoin"
}
