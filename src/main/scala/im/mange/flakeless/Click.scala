package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object Click {
  def apply(flakeless: Flakeless, by: By): Unit = {
    apply(Body(flakeless.rawWebDriver), by, Some(flakeless))
  }

  def apply(in: WebElement, by: By, flakeless: Option[Flakeless] = None): Unit = {
    new Click(flakeless, in, by).execute()
  }
}

private class Click(flakeless: Option[Flakeless], in: WebElement, by: By) {
  def execute(): Unit = {
    WaitForInteractableElement(in, by,

      description = e => describe(in, by, e),

      action = e => {
        e.click()
        flakeless.foreach(_.record("> " + describe(in, by, e)))
      }
    )
  }

  private def describe(in: WebElement, by: By, e: WebElement) = {
    Description("Click", in, by).describe(e)
  }
}
