package im.mange.flakeless

import im.mange.flakeless.innards._
import org.openqa.selenium.WebDriver

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