package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object ClearInputAndSendKeys {
  def apply(flakeless: Flakeless, by: By, keysToSend: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, keysToSend, Some(flakeless))
  }

  def apply(in: WebElement, by: By, keysToSend: String, flakeless: Option[Flakeless] = None): Unit = {
    WaitForInteractableElement(flakeless, in, by,

      description = e => Description("ClearInputAndSendKeys", in, by, args = Map("keysToSend" -> keysToSend.toString())).describe(e),

      action = e => {
        e.clear()
        e.sendKeys(keysToSend)
      }

    )
  }
}
