package im.mange.flakeless

import org.openqa.selenium.WebDriver

case class Flakeless(rawWebDriver: WebDriver) {
  private val currentFlightNumber = AtomicIntCounter()
  private val fdr = FlightDataRecorder()

  def newFlight(description: Option[String] = None) {
    currentFlightNumber.next
  }

  def record(success: Boolean, data: String) {
    fdr.record(currentFlightNumber.value, (if (success) "/" else "x" ) + " " + currentFlightNumber.value + data)
  }

  def flightData(flight: Int = currentFlightNumber.value) = fdr.data(flight)
}

case class DataPoint(flightNumber: Int, when: Long, what: Any)

case class FlightDataRecorder() {
  private val dataByFlight: scala.collection.concurrent.TrieMap[Int, Seq[DataPoint]] =
    new scala.collection.concurrent.TrieMap()

  def record(flightNumber: Int, data: String) {
    val current = dataByFlight.getOrElse(flightNumber, Seq.empty[DataPoint])
    dataByFlight.update(flightNumber, current :+ DataPoint(flightNumber, System.currentTimeMillis(), data))
  }

  def data(flightNumber: Int): Seq[DataPoint] = dataByFlight.getOrElse(flightNumber, Nil)
}

case class AtomicIntCounter(start: Int = 1) {
  private var count = start - 1

  def next = synchronized {
    count += 1
    count
  }

  def value = count
}