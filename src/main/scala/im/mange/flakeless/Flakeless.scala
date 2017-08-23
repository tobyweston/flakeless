package im.mange.flakeless

import im.mange.flakeless.innards.{AtomicIntCounter, Command, Context, FlightDataRecorder}
import org.openqa.selenium.WebDriver

//TODO: in Config, have option to forget previous flight data when calling newFlight
//TODO: store summary on reset for later ...
object FlightNumber {
  private val currentFlightNumberCounter = AtomicIntCounter()

  def next = currentFlightNumberCounter.next
}

case class Flakeless(rawWebDriver: WebDriver, config: Config = Config()) {
  private val fdr = FlightDataRecorder()
  private var currentFlightNumber = -1 //TODO: should be Option instead

  def newFlight(description: Option[String] = None) {
    currentFlightNumber = FlightNumber.next
    fdr.reset() // if config.resetOnNewFlight whateva
    description.foreach(d => fdr.record(currentFlightNumber, d, None, isError = false))
  }

  def record(command: Command, context: Context) {
    fdr.record(currentFlightNumber, command, context)
  }

  def inflightAnnouncement(description: String, log: Option[List[String]] = None, isError: Boolean = false) {
    fdr.record(currentFlightNumber, description, log, isError)
  }

  def flightData(flight: Int = currentFlightNumber) = fdr.data(flight)
  def jsonFlightData(flight: Int = currentFlightNumber) = fdr.jsonData(flight)
  def getCurrentFlightNumber = currentFlightNumber
}