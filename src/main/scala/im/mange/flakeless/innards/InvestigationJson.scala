package im.mange.flakeless.innards

import io.circe._
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._
import org.joda.time.DateTime

private [flakeless] object InvestigationJson {

  implicit val dateTimeEncoder: Encoder[DateTime] = Encoder.instance(a => a.getMillis.asJson)
  implicit val encoder: Encoder[Investigation] = deriveEncoder[Investigation]

  def serialise(r: Seq[Investigation]) = {
    r.asJson.noSpaces
  }

}
