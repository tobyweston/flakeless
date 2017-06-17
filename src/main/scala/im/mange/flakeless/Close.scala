package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Context}

import scala.collection.JavaConverters._

object Close {
  def apply(flakeless: Flakeless): Unit = {
    val rawWebDriver = flakeless.rawWebDriver

    rawWebDriver.getWindowHandles.asScala.foreach(windowHandle => {
      rawWebDriver.switchTo().window(windowHandle)
      rawWebDriver.close()
    })

    rawWebDriver.quit()
    flakeless.record(Command("Close", None, None), Context().succeeded())
  }
}
