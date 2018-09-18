package com.test.tookitaki.utils

/**
  * Created by sridhara.g on 9/18/18.
  */
object PriceFormatter {

  def format(value: Float): Float = {
    (f"$value%1.2f").toFloat
  }
}
