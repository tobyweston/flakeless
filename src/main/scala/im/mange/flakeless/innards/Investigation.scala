package im.mange.flakeless.innards

import org.joda.time.DateTime

case class Investigation(flightNumber: Int, suite: String, test: String, success: Boolean,
                         started: Option[DateTime], finished: Option[DateTime],
                         firstInteraction: Option[DateTime],
                         grossDurationMillis: Option[Long], netDurationMillis: Option[Long], dataPointCount: Int)
