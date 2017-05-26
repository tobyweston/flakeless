package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

//TODO: should this be Enter? it isnt in webdriver
object SendKeys {
  def apply(flakeless: Flakeless, by: By, keysToSend: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, keysToSend, Some(flakeless))
  }

  def apply(flakeless: Flakeless, by: By, keysToSend: CharSequence*): Unit = {
    apply(Body(flakeless.rawWebDriver), by, (keysToSend:_*).toString(), Some(flakeless))
  }

  def apply(in: WebElement, by: By, keysToSend: String, flakeless: Option[Flakeless] = None): Unit = {
    new SendKeys(flakeless, in, by, keysToSend).execute()
  }
}

private class SendKeys(flakeless: Option[Flakeless], in: WebElement, by: By, keysToSend: String) {
  def execute(): Unit = {
    WaitForInteractableElement(flakeless, in, by,

      description = e => Description("SendKeys", in, by, args = Map("keysToSend" -> keysToSend)).describe(e),

      action = e => e.sendKeys(keysToSend)
    )
  }
}