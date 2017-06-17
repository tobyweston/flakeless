package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless

object Execute {
  def apply(flakeless: Option[Flakeless], executable: Executable): Unit = {
    val context = Context()

    try {
      executable.execute(context)
      context.succeeded()
      flakeless.foreach(_.record(true, executable.command.describe, Some(context)))
    }

    catch {
      case e: Exception => {
        flakeless.foreach(_.record(false, executable.command.describe, Some(context)))
        throw e
      }

    }
  }
}
