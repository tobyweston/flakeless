package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebElement}

//TODO: should this be Enter? it isnt in webdriver
//TODO: this should share with SendKeys
object ClearInputAndSendKeys {
  def apply(flakeless: Flakeless, by: By, keysToSend: CharSequence): Unit = {
    apply(Body(flakeless.rawWebDriver), by, List(keysToSend), Some(flakeless))
  }
  def apply(flakeless: Flakeless, by: By, keysToSend: List[CharSequence]): Unit = {
    apply(Body(flakeless.rawWebDriver), by, keysToSend, Some(flakeless))
  }

  def apply(in: WebElement, by: By, keysToSend: List[CharSequence], flakeless: Option[Flakeless] = None): Unit = {
    new ClearInputAndSendKeys(flakeless, in, by, keysToSend).execute()
  }
}

private class ClearInputAndSendKeys(flakeless: Option[Flakeless], in: WebElement, by: By, keysToSend: List[CharSequence]) {
  def execute(): Unit = {
    WaitForInteractableElement(flakeless,
      Command("ClearInputAndSendKeys", Some(in), by, args = Map("keysToSend" -> keysToSend.mkString)),

      description = e => Description().describeActual(e),

      action = e => {
        e.clear()
        e.sendKeys(keysToSend:_*)
      }

    )
  }
}