package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebElement}

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
    WaitForInteractableElement(flakeless,
      Command("Click", in, by),
      description = e => Description().describeActual(e),
      action = e => e.click()
    )
  }
}
