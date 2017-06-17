package im.mange.flakeless.innards

private [flakeless] case class FlightDataRecorder() {
  private val dataByFlightNumber: scala.collection.concurrent.TrieMap[Int, Seq[DataPoint]] =
    new scala.collection.concurrent.TrieMap()

  def record(flightNumber: Int, description: String) {
    val current = dataByFlightNumber.getOrElse(flightNumber, Seq.empty[DataPoint])
    dataByFlightNumber.update(flightNumber, current :+ DataPoint(flightNumber, System.currentTimeMillis(), Some(description), None, None))
  }
  def record(flightNumber: Int, command: Command, context: Context) {
    val current = dataByFlightNumber.getOrElse(flightNumber, Seq.empty[DataPoint])
    dataByFlightNumber.update(flightNumber, current :+ DataPoint(flightNumber, System.currentTimeMillis(), None, Some(command), Some(context)))
  }

  def data(flightNumber: Int): Seq[DataPoint] = dataByFlightNumber.getOrElse(flightNumber, Nil)
}
