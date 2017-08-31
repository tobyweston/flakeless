package im.mange.flakeless.reporting.innards

import org.joda.time.DateTime

/**
  * Created by pall on 31/08/2017.
  */
case class FlightInvestigation(flightNumber: Int, name: Option[String], started: Option[DateTime], finished: Option[DateTime])
