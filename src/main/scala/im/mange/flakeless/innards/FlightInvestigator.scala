package im.mange.flakeless.innards

import org.joda.time.Duration

private [flakeless] object FlightInvestigator {
  private val investigationByFlightNumber: scala.collection.concurrent.TrieMap[Int, Investigation] =
    new scala.collection.concurrent.TrieMap()

  def investigate(flightNumber: Int, flightDataRecorder: FlightDataRecorder): Unit = {
    update(flightNumber, createInvestigation(flightNumber, flightDataRecorder))
  }

  def jsonData = InvestigationJson.serialise(investigationByFlightNumber.values.toList)

  //TODO; we could actually have this from the start .. and not need to poke about to get start and finish
  private def createInvestigation(flightNumber: Int, flightDataRecorder: FlightDataRecorder) = {
    val flightData: FlightDataRecord = flightDataRecorder.data(flightNumber)
    val name = flightData.name
    val started = flightData.started
    val firstInteraction = flightData.dataPoints.headOption.map(_.when)
    val finished = flightData.dataPoints.reverse.headOption.map(_.when)

    val grossDuration = (started, finished) match {
      case (Some(start), Some(finish)) => Some(new Duration(start, finish).getMillis)
      case _ => None
    }

    val netDuration = (firstInteraction, finished) match {
      case (Some(start), Some(finish)) => Some(new Duration(start, finish).getMillis)
      case _ => None
    }

    Investigation(flightNumber, name, started, finished, firstInteraction, grossDuration, netDuration, flightData.dataPoints.size)
  }

  private def update(flightNumber: Int, investigation: Investigation): Unit = {
    investigationByFlightNumber.update(flightNumber, investigation)
  }
}
