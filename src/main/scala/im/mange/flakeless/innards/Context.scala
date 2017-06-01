package im.mange.flakeless.innards

case class Context() {
  private var failures = List[String]()
  private var success: Option[Boolean] = None

  def remember(result: Boolean, value: String) = {
    if (result) {
      success = Some(true)
    } else {
      failures = value :: failures
    }
  }
}
