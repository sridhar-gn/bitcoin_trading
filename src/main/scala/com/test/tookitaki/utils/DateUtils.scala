package com.test.tookitaki.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar

/**
  * Created by sridhara.g on 9/16/18.
  */
object DateUtils {

  val inputFormat = new SimpleDateFormat("yyyy-MM-dd")

  /*Get the current timestamp*/
  def Now: java.sql.Timestamp = new java.sql.Timestamp(System.currentTimeMillis())

  def timeStamp(dateString: String): Timestamp = {
    val date =  inputFormat.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.setTime(date)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    new Timestamp(calendar.getTime.getTime)
  }

  def getDifferenceBetweenDates(fromDate: Timestamp, toDate: Timestamp): Int = {
    ((toDate.getTime - fromDate.getTime) / (1000*60*60*24)).toInt
  }

  def getTimestampByAddingDays(days: Int): Long = {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, days)
    getDateFromTimestamp(new Timestamp(cal.getTime.getTime)).getTime
  }

  def getDateFromTimestamp(timeStamp: Timestamp): Timestamp = {
    val calendar = Calendar.getInstance()
    calendar.setTime(timeStamp)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    new Timestamp(calendar.getTime.getTime)
  }
}
