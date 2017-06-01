package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless

/**
  * Created by pall on 01/06/2017.
  */
object Execute {
  def apply(flakeless: Option[Flakeless], executable: Executable): Unit = {
    //TODO: probably should pass the context in here instead
    executable.execute(flakeless)
  }
}
