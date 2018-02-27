package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, SearchContext, WebElement}

//TODO: should this be Enter? it isnt in webdriver
//TODO: this should definitely share with SendKeys
object ClearInputAndSendKeys {
  def apply(flakeless: Flakeless, delayMillis: Option[Int], by: By, keysToSend: CharSequence): Unit = {
    apply(flakeless.rawWebDriver, delayMillis, by, List(keysToSend), Some(flakeless))
  }

  def apply(flakeless: Flakeless, delayMillis: Option[Int], by: By, keysToSend: List[CharSequence]): Unit = {
    apply(flakeless.rawWebDriver, delayMillis, by, keysToSend, Some(flakeless))
  }

  def apply(in: SearchContext, delayMillis: Option[Int], by: By, keysToSend: List[CharSequence], flakeless: Option[Flakeless] = None): Unit = {
    new ClearInputAndSendKeys(flakeless, in, by, keysToSend, delayMillis).execute()
  }
}

private class ClearInputAndSendKeys(flakeless: Option[Flakeless], in: SearchContext, by: By, keysToSend: List[CharSequence], delayMillis: Option[Int]) {
  def execute(): Unit = {
    WaitForInteractableElement(flakeless,
      Command("ClearInputAndSendKeys", Some(in), Some(by), args = Map("keysToSend" -> keysToSend.mkString)),

      description = e => Description().describeActual(e),

      action = e => {
        e.clear()
        delayMillis match {
          case None => e.sendKeys(keysToSend:_*)

          case Some(delay) => keysToSend.toString.foreach(k => {
            e.sendKeys(k.toString)
            Thread.sleep(delay)
          })
        }
      }

    )
  }
}
