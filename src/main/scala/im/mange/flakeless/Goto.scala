package im.mange.flakeless

import im.mange.flakeless.innards.Context

object Goto {
  def apply(flakeless: Flakeless, url: String): Unit = {
    //TODO: turn me into a proper Command and Execute me
    flakeless.rawWebDriver.get(url)
    flakeless.record(true, s"Goto $url", Some(Context()))
  }
}
