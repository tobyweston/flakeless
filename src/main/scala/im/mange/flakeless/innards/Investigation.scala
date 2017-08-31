package im.mange.flakeless.innards

import org.joda.time.DateTime

case class Investigation(flightNumber: Int, name: Option[String], started: Option[DateTime], finished: Option[DateTime], durationMillis: Option[Long])
