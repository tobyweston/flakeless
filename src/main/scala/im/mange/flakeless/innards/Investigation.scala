package im.mange.flakeless.innards

import org.joda.time.DateTime

case class Investigation(flightNumber: Int, name: String, started: Option[DateTime], finished: Option[DateTime], firstInteraction: Option[DateTime], durationMillis: Option[Long], durationMillis2: Option[Long])
