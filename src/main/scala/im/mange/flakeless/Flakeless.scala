package im.mange.flakeless

import im.mange.flakeless.innards.{AtomicIntCounter, Command, Context, FlightDataRecorder}
import org.openqa.selenium.WebDriver

//TODO: in Config, have option to forget prevoius flight data when calling newFlight
case class Flakeless(rawWebDriver: WebDriver, config: Config = Config()) {
  private val currentFlightNumberCounter = AtomicIntCounter()
  private val fdr = FlightDataRecorder()

  def newFlight(description: Option[String] = None) {
    currentFlightNumberCounter.next
    fdr.reset() // if config.resetOnNewFlight whateva
    description.foreach(d => fdr.record(currentFlightNumber, d))
  }

  def record(command: Command, context: Context) {
    fdr.record(currentFlightNumber, command, context)
  }

  def inflightAnnouncement(value: String) {
    fdr.record(currentFlightNumber, value)
  }

  def flightData(flight: Int = currentFlightNumber) = fdr.data(flight)
  def jsonFlightData(flight: Int = currentFlightNumber) = fdr.jsonData(flight)

//  def writeFlightData(flight: Int = currentFlightNumber) {
//    fdr.write(flight)
//  }

  def currentFlightNumber = currentFlightNumberCounter.value
}



