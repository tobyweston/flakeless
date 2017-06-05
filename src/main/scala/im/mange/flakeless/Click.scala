package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, Intention, WaitForInteractableElement}
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
    val intention = Intention("Click", in, by)

    WaitForInteractableElement(flakeless, intention,

      description = e => {
        Description().describeActual(e)
      },

      action = e => e.click()
    )
  }
}
