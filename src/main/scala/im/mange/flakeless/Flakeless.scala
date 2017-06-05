package im.mange.flakeless

import im.mange.flakeless.innards.{AtomicIntCounter, Context, FlightDataRecorder}
import org.openqa.selenium.WebDriver

case class Flakeless(rawWebDriver: WebDriver) {
  private val currentFlightNumberCounter = AtomicIntCounter()
  private val fdr = FlightDataRecorder()

  def newFlight(description: Option[String] = None) {
    currentFlightNumberCounter.next
  }

  def record(success: Boolean, data: String, context: Option[Context]) {
    fdr.record(currentFlightNumber, (if (success) "/" else "x" ) + " " + data + " " + context)
  }

  def inflightAnnouncement(value: String) {
    fdr.record(currentFlightNumber, value)
  }

  def flightData(flight: Int = currentFlightNumber) = fdr.data(flight)

  def currentFlightNumber = currentFlightNumberCounter.value
}



