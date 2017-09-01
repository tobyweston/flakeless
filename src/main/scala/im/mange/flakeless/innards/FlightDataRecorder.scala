package im.mange.flakeless.innards

import org.joda.time.DateTime

//TODO: pull out the json bit to another thing
//TODO: might be able to drop the whole flight number thing from the json soon

case class FlightDataRecord(started: Option[DateTime], finished: Option[DateTime], dataPoints: Seq[DataPoint])

private [flakeless] case class FlightDataRecorder() {
  private var dataByFlightNumber: scala.collection.concurrent.TrieMap[Int, FlightDataRecord] =
    new scala.collection.concurrent.TrieMap()

  def reset() {
    dataByFlightNumber = new scala.collection.concurrent.TrieMap()
  }

  def record(flightNumber: Int, description: String, log: Option[List[String]], isError: Boolean) {
    append(flightNumber, DataPoint(flightNumber, DateTime.now, Some(description), None, Some(Context(Nil, Some(!isError))), log))
  }

  def record(flightNumber: Int, command: Command, context: Context) {
    append(flightNumber, DataPoint(flightNumber, DateTime.now, None, Some(command.report), Some(context), None))
  }

  def jsonData(flight: Int) = DataPointJson.serialise(data(flight).dataPoints)

  def data(flightNumber: Int): FlightDataRecord = dataByFlightNumber.get(flightNumber).getOrElse(throw new RuntimeException(s"Not Record for flightnumber: $flightNumber"))

  private def append(flightNumber: Int, dataPoint: DataPoint): Unit = {
    val current = dataByFlightNumber.getOrElse(flightNumber, FlightDataRecord(Some(DateTime.now()), None, Seq.empty[DataPoint]))
    dataByFlightNumber.update(flightNumber, current.copy(dataPoints = current.dataPoints :+ dataPoint))
  }
}