package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Context, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebElement}

object Goto {
  def apply(flakeless: Flakeless, url: String): Unit = {
    flakeless.rawWebDriver.get(url)
    flakeless.record(true, s"Goto $url", Some(Context()))
  }
}
