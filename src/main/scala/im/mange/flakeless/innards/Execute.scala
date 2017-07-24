package im.mange.flakeless.innards

import im.mange.flakeless.{Config, Flakeless}

object Execute {
  def apply(flakeless: Option[Flakeless], executable: Executable): Unit = {
    val context = Context()

    try {
      val config = flakeless.fold(Config())(_.config)
      executable.execute(context, config)
      context.succeeded()
      flakeless.foreach(_.record(executable.command, context))
    }

    catch {
      case e: Exception => {
        flakeless.foreach(_.record(executable.command, context))
        context.success = Some(false)
        throw e
      }
    }
  }
}
