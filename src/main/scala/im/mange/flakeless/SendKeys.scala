package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, Command, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

//TODO: should this be Enter? it isnt in webdriver
//TODO: this should share with ClearAndSendKeys
object SendKeys {
  def apply(flakeless: Flakeless, by: By, keysToSend: CharSequence): Unit = {
    apply(Body(flakeless.rawWebDriver), by, List(keysToSend), Some(flakeless))
  }

  def apply(flakeless: Flakeless, by: By, keysToSend: List[CharSequence]): Unit = {
    apply(Body(flakeless.rawWebDriver), by, keysToSend, Some(flakeless))
  }

  def apply(in: WebElement, by: By, keysToSend: List[CharSequence], flakeless: Option[Flakeless] = None): Unit = {
    new SendKeys(flakeless, in, by, keysToSend).execute()
  }
}

private class SendKeys(flakeless: Option[Flakeless], in: WebElement, by: By, keysToSend: List[CharSequence]) {
  def execute(): Unit = {
    val intention = Command("SendKeys", in, by, args = Map("keysToSend" -> keysToSend.mkString))

    WaitForInteractableElement(flakeless, intention,

      description = e => {
        Description().describeActual(e)
      },

      action = e => e.sendKeys(keysToSend:_*)
    )
  }
}