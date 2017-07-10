package im.mange.flakeless

import im.mange.flakeless.innards.{AtomicIntCounter, Command, Context, FlightDataRecorder}
import org.openqa.selenium.WebDriver

case class Flakeless(rawWebDriver: WebDriver) {
  private val currentFlightNumberCounter = AtomicIntCounter()
  private val fdr = FlightDataRecorder()

  def newFlight(description: Option[String] = None) {
    currentFlightNumberCounter.next
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



