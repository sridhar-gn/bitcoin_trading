package com.test.tookitaki.mapper

import com.test.tookitaki.exceptions.BitcoinPriceNotFoundException
import com.test.tookitaki.response.{BitCoinResponse, GetBitCoinResponse, PriceDetails}
import com.test.tookitaki.utils.DateUtils

/**
  * Created by sridhara.g on 9/17/18.
  */
trait BitCoinMapper {

  /*map to entity price details*/
  def toPriceDetailsEntity(bitCoinResponse: BitCoinResponse): Seq[PriceDetails] = {
    if(bitCoinResponse.data.prices.isEmpty)
      throw new BitcoinPriceNotFoundException

    bitCoinResponse.data.prices.map(p => {
      PriceDetails(price = p.price, time = DateUtils.timeStamp(p.time))
    })
  }
}
