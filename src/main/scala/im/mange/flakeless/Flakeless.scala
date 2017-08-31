package im.mange.flakeless

import im.mange.flakeless.innards._
import org.joda.time.{DateTime, Duration}
import org.openqa.selenium.WebDriver

//TODO: in Config, have option to forget previous flight data when calling newFlight
//TODO: store summary on reset for later ...
object FlightNumber {
  private val currentFlightNumberCounter = AtomicIntCounter()

  def next = currentFlightNumberCounter.next
}

case class FlightInvestigation(flightNumber: Int, name: Option[String], started: Option[DateTime], finished: Option[DateTime])

object FlightInvestigator {
  private val investigationByFlightNumber: scala.collection.concurrent.TrieMap[Int, FlightInvestigation] =
    new scala.collection.concurrent.TrieMap()

  def investigate(flightNumber: Int, flightDataRecorder: FlightDataRecorder): Unit = {
    update(flightNumber, createInvestigation(flightNumber, flightDataRecorder))
  }

  def summarise() = {
    val keys = investigationByFlightNumber.keys.toList.sorted

    println(s"Flakeless Summary at ${DateTime.now()} for ${keys.length} test(s)")

    keys.foreach(k => {
      val i = investigationByFlightNumber(k)
      val duration = (i.started, i.finished) match {
        case (Some(start), Some(finish)) => new Duration(start, finish).getMillis.toString
        case _ => "???"
      }
      println(s"${i.flightNumber},${i.name.getOrElse("???")},$duration")
    } )
  }

  //TODO; we could actually have this from the start .. and not need to poke about to get start and finish
  private def createInvestigation(flightNumber: Int, flightDataRecorder: FlightDataRecorder) = {
    val flightData = flightDataRecorder.data(flightNumber)
    val started = flightData.headOption.map(_.when)
    val name = flightData.headOption.flatMap(_.description)
    val finished = flightData.reverse.headOption.map(_.when)
    FlightInvestigation(flightNumber, name, started, finished)
  }

  private def update(flightNumber: Int, investigation: FlightInvestigation): Unit = {
    investigationByFlightNumber.update(flightNumber, investigation)
  }
}

case class Flakeless(rawWebDriver: WebDriver, config: Config = Config()) {
  private val fdr = FlightDataRecorder()
  private var currentFlightNumber: Option[Int] = None

  def startFlight(description: String) {
    currentFlightNumber = Some(FlightNumber.next)
    fdr.reset()
    currentFlightNumber.foreach(fdr.record(_, description, None, isError = false))
  }

  def stopFlight() {
    currentFlightNumber.foreach(FlightInvestigator.investigate(_, fdr))
  }

  def record(command: Command, context: Context) {
    currentFlightNumber.foreach(fdr.record(_, command, context))
  }

  def inflightAnnouncement(description: String, log: Option[List[String]] = None, isError: Boolean = false) {
    currentFlightNumber.foreach(fdr.record(_, description, log, isError))
  }

  def jsonFlightData(flightNumber: Int) = fdr.jsonData(flightNumber)
  def getCurrentFlightNumber = currentFlightNumber.getOrElse(throw new RuntimeException("No flight number, perhaps you forgot to call newFlight"))
}