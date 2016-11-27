package im.mange.flakeless

import im.mange.flakeless.innards.{Body, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object ClearInputAndSendKeys {
  def apply(webDriver: WebDriver, by: By, keysToSend: CharSequence*): Unit = {
    apply(Body(webDriver), by, keysToSend: _*)
  }

  def apply(in: WebElement, by: By, keysToSend: CharSequence*): Unit = {
    WaitForInteractableElement(in, by,
      description = e => s"ClearInputAndSendKeys\n| in: $in| $by\n| $keysToSend",
      action = e => {
        e.clear()
        e.sendKeys(keysToSend: _*)
      }
    )
  }
}
