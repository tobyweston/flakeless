package im.mange.flakeless.innards

import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._
import org.joda.time.DateTime

private[flakeless] object FlightDataRecordJson {

  implicit val dateTimeEncoder: Encoder[DateTime] = Encoder.instance(a => a.getMillis.asJson)
  implicit val encoder: Encoder[FlightDataRecord] = deriveEncoder[FlightDataRecord]

  def serialise(r: FlightDataRecord) = {
    encoder(r).spaces2
  }
}
