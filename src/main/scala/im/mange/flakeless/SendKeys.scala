package im.mange.flakeless

import org.openqa.selenium.{By, WebDriver, WebElement}

/**
  * Created by pall on 10/11/2016.
  */
object SendKeys {
  def apply(webDriver: WebDriver, by: By, keysToSend: CharSequence*): Unit = {
    apply(Body(webDriver), by, keysToSend: _*)
  }

  def apply(in: WebElement, by: By, keysToSend: CharSequence*): Unit = {
    WaitForInteractableElement(in, by,
      description = e => s"SendKeys\n| in: $in| $by\n| $keysToSend",
      action = e => e.sendKeys(keysToSend: _*)
    )
  }
}
