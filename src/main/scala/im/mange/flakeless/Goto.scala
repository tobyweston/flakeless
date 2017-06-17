package im.mange.flakeless

import im.mange.flakeless.innards._

object Goto {
  def apply(flakeless: Flakeless, url: String): Unit = {
    new Goto(flakeless, url).execute()
  }
}

private class Goto(flakeless: Flakeless, url: String) {
  def execute(): Unit = {
    WithoutElement(flakeless,
      Command("Goto", None, None, args = Map {"url" -> url}),
      action = d => d.get(url)
    )
  }
}
