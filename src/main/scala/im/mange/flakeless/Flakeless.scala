package im.mange.flakeless

import im.mange.flakeless.innards.{AtomicIntCounter, FlightDataRecorder}
import org.openqa.selenium.WebDriver

case class Flakeless(rawWebDriver: WebDriver) {
  private val currentFlightNumberCounter = AtomicIntCounter()
  private val fdr = FlightDataRecorder()

  def newFlight(description: Option[String] = None) {
    currentFlightNumberCounter.next
  }

  def record(success: Boolean, data: String) {
    fdr.record(currentFlightNumber, (if (success) "/" else "x" ) + " " + data)
  }

  def inflightAnnouncement(value: String) {
    fdr.record(currentFlightNumber, value)
  }

  def flightData(flight: Int = currentFlightNumber) = fdr.data(flight)

  def currentFlightNumber = currentFlightNumberCounter.value
}

case class DataPoint(flightNumber: Int, when: Long, what: Any)

case class Context() {
  private var failures = List[String]()
  private var success: Option[Boolean] = None

  def remember(result: Boolean, value: String) = {
    if (result) {
      success = Some(true)
    } else {
      failures = value :: failures
    }
  }
}