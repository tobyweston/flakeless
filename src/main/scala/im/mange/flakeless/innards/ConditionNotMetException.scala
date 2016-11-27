package im.mange.flakeless.innards

private [flakeless] class ConditionNotMetException(message: String) extends RuntimeException(message) {
  def this(conditionToCheck: String, millis: Long) = this(conditionToCheck + " (not met within " + millis + " millis)")
}
