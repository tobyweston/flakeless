package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, SearchContext}

//TODO: should this be Enter? it isnt in webdriver
//TODO: this should share with ClearAndSendKeys - all that is different is bool: clear
object SendKeys {
  def apply(flakeless: Flakeless, by: By, keysToSend: CharSequence): Unit = {
    apply(flakeless.rawWebDriver, by, List(keysToSend), Some(flakeless))
  }

  def apply(flakeless: Flakeless, by: By, keysToSend: List[CharSequence]): Unit = {
    apply(flakeless.rawWebDriver, by, keysToSend, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, keysToSend: List[CharSequence], flakeless: Option[Flakeless] = None): Unit = {
    new SendKeys(flakeless, in, by, keysToSend).execute()
  }
}

private class SendKeys(flakeless: Option[Flakeless], in: SearchContext, by: By, keysToSend: List[CharSequence]) {
  def execute(): Unit = {
    WaitForInteractableElement(flakeless,
      Command("SendKeys", Some(in), Some(by), args = Map("keysToSend" -> keysToSend.mkString)),
      description = e => Description().describeActual(e),
      action = e => e.sendKeys(keysToSend:_*)
    )
  }
}
