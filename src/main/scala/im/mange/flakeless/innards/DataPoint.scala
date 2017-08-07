package im.mange.flakeless.innards

import org.joda.time.DateTime

case class DataPoint(flightNumber: Int, when: DateTime, description: Option[String],
                     command: Option[ReportCommand], context: Option[Context], log: Option[List[String]])
