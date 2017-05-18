package im.mange.flakeless

import org.openqa.selenium.WebDriver

case class Flakeless(driver: WebDriver) {
  private val flight = new AtomicIntCounter(0)
  private val fdr = FlightDataRecorder()

  def nextFlight(description: Option[String] = None) {
    flight.next
  }

  def record(data: String) {
    fdr.record(flight.value, data)
  }
}

case class DataPoint(who: Int, when: Long, what: Any)

case class FlightDataRecorder() {
  private val dataByFlight: scala.collection.concurrent.TrieMap[Int, Seq[DataPoint]] =
    new scala.collection.concurrent.TrieMap()

  def record(flight: Int, data: String) {
    val current = dataByFlight.getOrElse(flight, Seq.empty[DataPoint])
    dataByFlight.update(flight, current :+ DataPoint(flight, System.currentTimeMillis(), data))
  }

  def data(flight: Int) = dataByFlight(flight)
}

class AtomicIntCounter(start: Int = 1) {
  private var count = start - 1

  def next = synchronized {
    count += 1
    count
  }

  def value = count
}