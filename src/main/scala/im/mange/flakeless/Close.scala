package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Context, WithoutElement}

import scala.collection.JavaConverters._

object Close {
  def apply(flakeless: Flakeless): Unit = {
    new Close(flakeless).execute()
  }
}

private class Close(flakeless: Flakeless) {
  def execute(): Unit = {
    WithoutElement(flakeless,
      Command("Close", None, None),
      action = d => {
        d.getWindowHandles.asScala.foreach(windowHandle => {
          d.switchTo().window(windowHandle)
          d.close()
        })
        d.quit()
      }
    )
  }
}

