package im.mange.flakeless.innards

import im.mange.flakeless.{Context, Flakeless}

trait Executable {
  def execute(context: Context, flakeless: Option[Flakeless])
}
