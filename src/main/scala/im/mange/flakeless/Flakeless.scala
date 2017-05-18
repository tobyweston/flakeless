package im.mange.flakeless

import org.openqa.selenium.WebDriver

case class Flakeless(webDriver: WebDriver) {
  private val currentFlight = new AtomicIntCounter(0)
  private val fdr = FlightDataRecorder()

  def nextFlight(description: Option[String] = None) {
    currentFlight.next
  }

  def record(data: String) {
    fdr.record(currentFlight.value.toString, data)
  }

  def data(flight: Int = currentFlight.value) = fdr.data(flight.toString)
}

case class DataPoint(flight: String, when: Long, what: Any)

case class FlightDataRecorder() {
  private val dataByFlight: scala.collection.concurrent.TrieMap[String, Seq[DataPoint]] =
    new scala.collection.concurrent.TrieMap()

  def record(flight: String, data: String) {
    val current = dataByFlight.getOrElse(flight, Seq.empty[DataPoint])
    dataByFlight.update(flight, current :+ DataPoint(flight, System.currentTimeMillis(), data))
  }

  def data(flight: String) = dataByFlight(flight)
}

class AtomicIntCounter(start: Int = 1) {
  private var count = start - 1

  def next = synchronized {
    count += 1
    count
  }

  def value = count
}