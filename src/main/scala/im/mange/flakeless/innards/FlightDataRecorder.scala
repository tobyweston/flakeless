package im.mange.flakeless.innards

import java.nio.file.Paths

import im.mange.little.file.Filepath
import im.mange.little.json.{LittleJodaSerialisers, LittleSerialisers}
import org.joda.time.DateTime
import org.json4s.{NoTypeHints, _}
import org.json4s.native.{JsonParser, Serialization}
import org.json4s.native.Serialization._
import org.json4s.native.JsonMethods._

//TODO: pull out the json bit to another thing
//TODO: might be able to drop the whole flight number thing from the json soon
private [flakeless] case class FlightDataRecorder() {
  private var dataByFlightNumber: scala.collection.concurrent.TrieMap[Int, Seq[DataPoint]] =
    new scala.collection.concurrent.TrieMap()

  def reset() {
    dataByFlightNumber = new scala.collection.concurrent.TrieMap()
  }

  def record(flightNumber: Int, description: String, log: Option[List[String]]) {
    append(flightNumber, DataPoint(flightNumber, DateTime.now, Some(description), None, None, log))
  }

  def record(flightNumber: Int, command: Command, context: Context) {
    append(flightNumber, DataPoint(flightNumber, DateTime.now, None, Some(command.report), Some(context), None))
  }

  def data(flightNumber: Int): Seq[DataPoint] = dataByFlightNumber.getOrElse(flightNumber, Nil)

  def jsonData(flight: Int) = Json.serialise(data(flight))

  private def append(flightNumber: Int, dataPoint: DataPoint): Unit = {
    val current = dataByFlightNumber.getOrElse(flightNumber, Seq.empty[DataPoint])
    dataByFlightNumber.update(flightNumber, current :+ dataPoint)
  }

  private def writeReport(flightNumber: Int, dataPoints: Seq[DataPoint], outputDirectory: String) {
    //TODO: we should delegate to the TestRunRegistry for this ...
//    val directory = Directory(outputDirectory)
//    if (!directory.exists) directory.createDirectory(force = true)
    val filepath = outputDirectory + "/" + flightNumber + ".json"
    val content = Json.serialise(dataPoints)
    Filepath.save(content, Paths.get(filepath))
  }
}


object Json {
  private val shoreditchFormats = Serialization.formats(NoTypeHints) ++ LittleSerialisers.all ++ LittleJodaSerialisers.all

  def serialise(r: Seq[DataPoint]) = {
    implicit val formats = shoreditchFormats
    pretty(render(JsonParser.parse(write(r))))
  }
}
