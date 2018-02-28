package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, SearchContext, WebElement}

object SendKeys {
  def apply(flakeless: Flakeless, by: By, clear: Boolean, keysToSend: CharSequence): Unit = {
    apply(flakeless.rawWebDriver, by, clear, keysToSend, Some(flakeless))
  }

  def apply(flakeless: Flakeless, by: By, clear: Boolean, keysToSend: List[CharSequence]): Unit = {
    apply(flakeless.rawWebDriver, by, clear, keysToSend.mkString, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, clear: Boolean, keysToSend: CharSequence, flakeless: Option[Flakeless] = None): Unit = {
    new SendKeys(flakeless, in, by, clear, List(keysToSend)).execute()
  }
}

private class SendKeys(flakeless: Option[Flakeless], in: SearchContext, by: By, clear: Boolean, keysToSend: List[CharSequence]) {
  def execute(): Unit = {
    WaitForInteractableElement(flakeless,
      Command((if (clear)"ClearAnd" else "") + "SendKeys", Some(in), Some(by), args = Map("keysToSend" -> keysToSend.mkString, "clear" -> (if (clear) "true" else "false"))),

      description = e => Description().describeActual(e),

      action = e => {
        if (clear) e.clear()

        flakeless.flatMap(_.config.sendKeysDelayMillis) match {
          case None => e.sendKeys(keysToSend:_*)

          case Some(delay) => keysToSend.mkString.toString.foreach(k => {
            e.sendKeys(k.toString)
            Thread.sleep(delay)
          })
        }
      }

    )
  }
}
