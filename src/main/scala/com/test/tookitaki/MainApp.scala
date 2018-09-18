package com.test.tookitaki

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{handleExceptions, withExecutionContext}
import org.slf4j.LoggerFactory
import scaldi.Injectable
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.test.tookitaki.controller.BitCoinController
import com.test.tookitaki.handlers.BitcoinExceptionHandler
import com.test.tookitaki.module.ApplicationModule

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

/**
  * Created by sridhara.g on 9/16/18.
  */
object MainApp extends App with Injectable with BitcoinExceptionHandler {
  val logger = LoggerFactory.getLogger(this.getClass)

  implicit val injector = new ApplicationModule().dependencies

  implicit val ex = inject[ExecutionContext]
  implicit val sysyem = inject[ActorSystem]
  implicit val materializer = inject[ActorMaterializer]

private val executor: ExecutionContextExecutor = ex.asInstanceOf[ExecutionContextExecutor]

  val route: Route = withExecutionContext(executor) {
    handleExceptions(bitcoinExceptionHandler){ new BitCoinController().route }
  }
  logger.info("Binding to 8084 port")
  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8084)
}
