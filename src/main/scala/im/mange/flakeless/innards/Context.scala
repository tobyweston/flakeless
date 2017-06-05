package im.mange.flakeless.innards

case class Context() {
  private var failures = List[String]()
  private var success: Option[Boolean] = None

  //TODO: have success and fail methods, since value is only for failures ...
  def remember(result: Boolean, value: String) = {
    if (result) {
      success = Some(true)
    } else {
      failures = value :: failures
    }
  }
}
