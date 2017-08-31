package im.mange.flakeless.innards

import org.joda.time.{DateTime, Duration}

private [flakeless] object FlightInvestigator {
  private val investigationByFlightNumber: scala.collection.concurrent.TrieMap[Int, Investigation] =
    new scala.collection.concurrent.TrieMap()

  def investigate(flightNumber: Int, flightDataRecorder: FlightDataRecorder): Unit = {
    update(flightNumber, createInvestigation(flightNumber, flightDataRecorder))
  }

  //TODO: this should die once proper report done
  def summarise() = {
    val keys = investigationByFlightNumber.keys.toList.sorted

    println(s"Flakeless Summary at ${DateTime.now()} for ${keys.length} test(s)")

    keys.foreach(k => {
      val i = investigationByFlightNumber(k)
      println(s"${i.flightNumber},${i.name.getOrElse("???")},${i.durationMillis.getOrElse("???")}")
    } )
  }

  def jsonData = InvestigationJson.serialise(investigationByFlightNumber.values.toList)

  //TODO; we could actually have this from the start .. and not need to poke about to get start and finish
  private def createInvestigation(flightNumber: Int, flightDataRecorder: FlightDataRecorder) = {
    val flightData = flightDataRecorder.data(flightNumber)
    val started = flightData.headOption.map(_.when)
    val name = flightData.headOption.flatMap(_.description)
    val finished = flightData.reverse.headOption.map(_.when)

    val duration = (started, finished) match {
      case (Some(start), Some(finish)) => Some(new Duration(start, finish).getMillis)
      case _ => None
    }

    Investigation(flightNumber, name, started, finished, duration)
  }

  private def update(flightNumber: Int, investigation: Investigation): Unit = {
    investigationByFlightNumber.update(flightNumber, investigation)
  }
}
