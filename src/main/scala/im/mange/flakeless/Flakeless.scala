package im.mange.flakeless

import im.mange.flakeless.innards.{AtomicIntCounter, FlightDataRecorder}
import org.openqa.selenium.WebDriver

case class Flakeless(rawWebDriver: WebDriver) {
  private val currentFlightNumber = AtomicIntCounter()
  private val fdr = FlightDataRecorder()

  def newFlight(description: Option[String] = None) {
    currentFlightNumber.next
  }

  def record(success: Boolean, data: String) {
    fdr.record(currentFlightNumber.value, (if (success) "/" else "x" ) + " " + data)
  }

  def flightData(flight: Int = currentFlightNumber.value) = fdr.data(flight)
}

case class DataPoint(flightNumber: Int, when: Long, what: Any)
