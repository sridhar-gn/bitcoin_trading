package com.test.tookitaki.module

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.test.tookitaki.client.{BitCoinClient, BitCoinClientImpl}
import com.test.tookitaki.http.{HttpClient, HttpClientImpl}
import com.test.tookitaki.service.{BitCoinService, BitCoinServiceImpl}
import com.test.tookitaki.trading.{TradingClient, TradingImpl}
import com.typesafe.config.{Config, ConfigFactory}
import scaldi.Module

import scala.concurrent.ExecutionContext

/**
  * Created by sridhara.g on 9/16/18.
  */
class ApplicationModule extends Module{

 val dependencies: Module =  appModule

 def appModule: Module = new Module {

  bind[Config] to ConfigFactory.load()

  bind[ActorSystem] to ActorSystem("tootkitaki", inject[Config]) destroyWith (_.terminate())

  bind[ActorMaterializer] to ActorMaterializer()(inject[ActorSystem])

  bind[ExecutionContext] to  inject[ActorSystem].dispatcher

  bind[BitCoinService] to new BitCoinServiceImpl()

  bind[HttpClient] to new HttpClientImpl()

  bind[BitCoinClient] to new BitCoinClientImpl()

  bind[TradingClient] to new TradingImpl
 }
}
