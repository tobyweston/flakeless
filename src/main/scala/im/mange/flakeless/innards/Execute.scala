package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless

object Execute {
  def apply(flakeless: Option[Flakeless], executable: Executable): Unit = {
    val context = Context()

    try {
      executable.execute(context)
      context.succeeded()
      flakeless.foreach(_.record(executable.command, context))
    }

    catch {
      case e: Exception => {
        flakeless.foreach(_.record(executable.command, context))
        throw e
      }
    }
  }
}
