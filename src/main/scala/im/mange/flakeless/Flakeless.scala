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

case class FlightInvestigation(flightNumber: Int, started: Option[DateTime], finished: Option[DateTime])

object FlightInvestigator {
  private val investigationByFlightNumber: scala.collection.concurrent.TrieMap[Int, FlightInvestigation] =
    new scala.collection.concurrent.TrieMap()

  def investigate(flightNumber: Int, flightDataRecorder: FlightDataRecorder): Unit = {
    update(flightNumber, createInvestigation(flightNumber, flightDataRecorder))
  }

  def summarise() = {
    val keys = investigationByFlightNumber.keys.toList.sorted
    println(s"Flakeless Summary for ${keys.length} tests")
    keys.map(k => {
      val i = investigationByFlightNumber(k)
      val duration = (i.started, i.finished) match {
        case (Some(start), Some(finish)) => new Duration(start, finish).getMillis.toString
        case _ => "???"
      }
      println(s"${i.flightNumber},$duration")} )
  }

  private def createInvestigation(flightNumber: Int, flightDataRecorder: FlightDataRecorder) = {
    val flightData = flightDataRecorder.data(flightNumber)
    val started = flightData.headOption.map(_.when)
    val finished = flightData.reverse.headOption.map(_.when)
    FlightInvestigation(flightNumber, started, finished)
  }

  private def update(flightNumber: Int, investigation: FlightInvestigation): Unit = {
    investigationByFlightNumber.update(flightNumber, investigation)
  }
}

case class Flakeless(rawWebDriver: WebDriver, config: Config = Config()) {
  private val fdr = FlightDataRecorder()
  private var currentFlightNumber: Option[Int] = None

  def newFlight(description: Option[String] = None) {
    currentFlightNumber.foreach(FlightInvestigator.investigate(_, fdr))
    currentFlightNumber = Some(FlightNumber.next)
    fdr.reset() // if config.resetOnNewFlight whateva
    description.foreach(d => currentFlightNumber.foreach(fdr.record(_, d, None, isError = false)))
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