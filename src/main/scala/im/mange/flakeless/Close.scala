package im.mange.flakeless

import scala.collection.JavaConverters._

object Close {
  def apply(flakeless: Flakeless): Unit = {
    flakeless.rawWebDriver.getWindowHandles.asScala.foreach(windowHandle => {
      flakeless.rawWebDriver.switchTo().window(windowHandle)
      flakeless.rawWebDriver.close()
    })

    flakeless.rawWebDriver.quit()
  }
}
