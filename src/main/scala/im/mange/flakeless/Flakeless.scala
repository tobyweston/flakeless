package im.mange.flakeless

import org.openqa.selenium.WebDriver

case class Flakeless(rawWebDriver: WebDriver) {
  private val currentFlightId = AtomicIntCounter()
  private val fdr = FlightDataRecorder()

  def newFlight(description: Option[String] = None) {
    currentFlightId.next
  }

  def record(success: Boolean, data: String) {
    fdr.record((if (success) "/" else "x" ) ++ " " ++ currentFlightId.value.toString, data)
  }

  def flightData(flight: Int = currentFlightId.value) = fdr.data(flight.toString)
}

case class DataPoint(flight: String, when: Long, what: Any)

case class FlightDataRecorder() {
  private val dataByFlight: scala.collection.concurrent.TrieMap[String, Seq[DataPoint]] =
    new scala.collection.concurrent.TrieMap()

  def record(flight: String, data: String) {
    val current = dataByFlight.getOrElse(flight, Seq.empty[DataPoint])
    dataByFlight.update(flight, current :+ DataPoint(flight, System.currentTimeMillis(), data))
  }

  def data(flight: String): Seq[DataPoint] = dataByFlight.getOrElse(flight, Nil)
}

case class AtomicIntCounter(start: Int = 1) {
  private var count = start - 1

  def next = synchronized {
    count += 1
    count
  }

  def value = count
}