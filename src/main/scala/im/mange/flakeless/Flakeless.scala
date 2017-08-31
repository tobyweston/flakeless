package im.mange.flakeless

import im.mange.flakeless.innards.{AtomicIntCounter, Command, Context, FlightDataRecorder}
import org.openqa.selenium.WebDriver

//TODO: in Config, have option to forget previous flight data when calling newFlight
//TODO: store summary on reset for later ...
object FlightNumber {
  private val currentFlightNumberCounter = AtomicIntCounter()

  def next = currentFlightNumberCounter.next
}

case class FlightInvestigation()

object FlightInvestigator {

  def investigate(flightNumber: Int, flightDataRecorder: FlightDataRecorder): Unit = {
//    FlightInvestigation(flightDataRecorder.)
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