package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless

object Execute {
  def apply(flakeless: Option[Flakeless], executable: Executable): Unit = {
    //TODO: ultimately don't pass flakeless, but do all the recording here ...
//    flakeless.foreach(_.record(result, value))
    //and record will take a context ...
    //TODO: and the exception throwing ...
    executable.execute(Context(), flakeless)
  }
}
