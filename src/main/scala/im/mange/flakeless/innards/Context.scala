package im.mange.flakeless.innards

//TODO: temporary, until we json-ise
case class Context(var failures: List[String] = Nil, var success: Option[Boolean] = None) {

  //TODO: have success and fail methods, since value is only for failures ...
  //TODO: we should record the time here too ...
  //TODO: and just the value ...
  def remember(result: Boolean, value: String) = {
    if (result) {
      success = Some(true)
    } else {
      failures = value :: failures
    }
  }
}
