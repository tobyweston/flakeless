package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebElement}

object Goto {
  def apply(flakeless: Flakeless, url: String): Unit = {
    flakeless.record("> " + toString)
    flakeless.rawWebDriver.get(url)
    flakeless.record("< " + toString)
  }
}
