package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebElement}

//TODO: decide if we really need the execute() stuff, we probably only need it for composites ... (i.e. integration)
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
