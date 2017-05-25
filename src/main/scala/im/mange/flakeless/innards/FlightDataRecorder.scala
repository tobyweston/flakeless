package im.mange.flakeless.innards

import im.mange.flakeless.DataPoint

private [flakeless] case class FlightDataRecorder() {
  private val dataByFlight: scala.collection.concurrent.TrieMap[Int, Seq[DataPoint]] =
    new scala.collection.concurrent.TrieMap()

  def record(flightNumber: Int, data: String) {
    val current = dataByFlight.getOrElse(flightNumber, Seq.empty[DataPoint])
    dataByFlight.update(flightNumber, current :+ DataPoint(flightNumber, System.currentTimeMillis(), data))
  }

  def data(flightNumber: Int): Seq[DataPoint] = dataByFlight.getOrElse(flightNumber, Nil)
}
