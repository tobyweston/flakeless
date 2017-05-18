package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object Click {
  def apply(flakeless: Flakeless, by: By): Unit = {
    flakeless.record("> " + toString)
    apply(Body(flakeless.webDriver), by)
    flakeless.record("< " + toString)
  }

  def apply(in: WebElement, by: By): Unit = {
    WaitForInteractableElement(in, by,

      description = e => Description("Click", in, by).describe(e),

      action = e => e.click()
    )
  }
}
