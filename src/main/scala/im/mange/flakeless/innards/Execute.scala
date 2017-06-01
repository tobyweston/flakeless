package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless

object Execute {
  def apply(flakeless: Option[Flakeless], executable: Executable): Unit = {
    val context = Context()

    try {
      //TODO: need description ...
      executable.execute(context, flakeless)
      context.remember(true, "")
      flakeless.foreach(_.record(true, "", Some(context)))
    }

    catch {
      case e: Exception => {
        flakeless.foreach(_.record(false, "", Some(context)))
        throw e
      }

    }
  }
}
