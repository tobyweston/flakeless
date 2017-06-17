package im.mange.flakeless.innards

//TODO: temporary, until we json-ise
case class Context(var failures: List[String] = Nil, var success: Option[Boolean] = None) {

  def succeeded(): Unit = {
    success = Some(true)
  }

  //TODO: we should record the time in these too ...
  def failed(value: String): Unit =  {
    failures = value :: failures
  }
}
