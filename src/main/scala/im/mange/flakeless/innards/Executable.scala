package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless

/**
  * Created by pall on 01/06/2017.
  */
trait Executable {
  def execute(flakeless: Option[Flakeless])
}
