package im.mange.flakeless.innards

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._
import org.joda.time.DateTime

case class DataPoint(flightNumber: Int, when: DateTime, description: Option[String],
                     command: Option[ReportCommand], context: Context, log: Option[List[String]])

object DataPoint {
  implicit val dateTimeEncoder: Encoder[DateTime] = Encoder.instance(a => a.getMillis.asJson)
  implicit val encoder: Encoder[DataPoint] = deriveEncoder[DataPoint]
}