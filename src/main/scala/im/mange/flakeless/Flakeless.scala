package im.mange.flakeless

import im.mange.flakeless.innards._
import org.openqa.selenium.WebDriver

case class Flakeless(rawWebDriver: WebDriver, config: Config = Config()) {
  private val fdr = FlightDataRecorder()
  private var currentFlightNumber: Option[Int] = None

  def startFlight(suite: String, test: String) {
    currentFlightNumber = Some(FlightNumber.next)
    currentFlightNumber.foreach(fdr.start(_, suite, test))
  }

  def stopFlight() {
    currentFlightNumber.foreach(fdr.stop(_))
  }

  def record(command: Command, context: Context) {
    currentFlightNumber.foreach(fdr.record(_, command, context))
  }

  def inflightAnnouncement(description: String, log: Option[List[String]] = None, isError: Boolean = false) {
    currentFlightNumber.foreach(fdr.record(_, description, log, isError))
  }

  def jsonFlightData(flightNumber: Int) = fdr.jsonData(flightNumber)
  def jsonAllFlightsData = FlightInvestigator.jsonData
  def getCurrentFlightNumber = currentFlightNumber.getOrElse(throw new RuntimeException("No flight number, perhaps you forgot to call startFlight"))
}