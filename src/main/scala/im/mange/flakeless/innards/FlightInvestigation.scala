package im.mange.flakeless.innards

import org.joda.time.DateTime

case class FlightInvestigation(flightNumber: Int, name: Option[String], started: Option[DateTime], finished: Option[DateTime])
