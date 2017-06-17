package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Context}

//TODO: feels like I should be a factory for a private class ... see: Click
object Goto {
  def apply(flakeless: Flakeless, url: String): Unit = {
    //TODO: turn me into a proper Command and Execute me
//    Command("Goto")
    flakeless.rawWebDriver.get(url)
//    flakeless.record(s"Goto $url", Context())
  }
}
