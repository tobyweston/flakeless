package im.mange.flakeless.innards

import org.joda.time.DateTime

//TODO: pull out the json bit to another thing
//TODO: might be able to drop the whole flight number thing from the json soon

case class FlightDataRecord(suite: String, test: String, started: Option[DateTime], finished: Option[DateTime], dataPoints: Seq[DataPoint])

private [flakeless] case class FlightDataRecorder() {
  private var dataByFlightNumber: scala.collection.concurrent.TrieMap[Int, FlightDataRecord] =
    new scala.collection.concurrent.TrieMap()

  def start(flightNumber: Int, suite: String, name: String) = {
    dataByFlightNumber = new scala.collection.concurrent.TrieMap()
    val record = FlightDataRecord(suite, name, Some(DateTime.now()), None, Seq.empty[DataPoint])
    dataByFlightNumber.update(flightNumber, record)
  }

  def stop(flightNumber: Int)= {
    val record = data(flightNumber).copy(finished = Some(DateTime.now))
    dataByFlightNumber.update(flightNumber, record)
    FlightInvestigator.investigate(flightNumber, this)
  }

  def record(flightNumber: Int, description: String, log: Option[List[String]], isError: Boolean) {
    update(flightNumber, DataPoint(flightNumber, DateTime.now, Some(description), None, Context(Nil, Some(!isError)), log))
  }

  def record(flightNumber: Int, command: Command, context: Context) {
    update(flightNumber, DataPoint(flightNumber, DateTime.now, None, Some(command.report), context, None))
  }

  def jsonData(flight: Int) = DataPointJson.serialise(data(flight).dataPoints)

  def data(flightNumber: Int): FlightDataRecord = dataByFlightNumber.get(flightNumber).getOrElse(throw new RuntimeException(s"Not Record for flightnumber: $flightNumber"))

  private def update(flightNumber: Int, dataPoint: DataPoint): Unit = {
    val current: FlightDataRecord = data(flightNumber)
    dataByFlightNumber.update(flightNumber, current.copy(dataPoints = current.dataPoints :+ dataPoint))
  }
}