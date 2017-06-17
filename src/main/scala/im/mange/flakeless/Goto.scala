package im.mange.flakeless

import im.mange.flakeless.innards._

object Goto {
  def apply(flakeless: Flakeless, url: String): Unit = {
    new Goto(flakeless, url).execute()
  }
}

private class Goto(flakeless: Flakeless, url: String) {
  def execute(): Unit = {
    JustDoIt(flakeless,
      Command("Goto", None, None),
      //TODO: these actual/description start to look a bit odd .. it's really a case of not found an interactable
      description = e => Description().describeActual(e),
      action = d => d.get(url)
    )
  }
}
