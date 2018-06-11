package im.mange.flakeless.innards

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

//TODO: temporary, until we json-ise
case class Context(var failures: List[String] = Nil, var success: Option[Boolean] = None) {

  def succeeded() = {
    success = Some(true)
    this
  }

  //TODO: we should record the time in these too ...
  def failed(value: String) = {
    failures = value :: failures
    this
  }
}

object Context {
  implicit val encoder: Encoder[Context] = deriveEncoder[Context]
}