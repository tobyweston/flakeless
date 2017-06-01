package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

object WaitForInteractableElement {
  def apply(flakeless: Option[Flakeless], in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) {

    Execute(flakeless,
      new WaitForInteractableElement(in, by, description, condition, action, mustBeDisplayed))
  }
}

private class WaitForInteractableElement(in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) extends Executable {

  def execute(context: Context) {
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(
      {
        val e = in.findElement(by)
        val result = (if (mustBeDisplayed) e.isDisplayed else true) && e.isEnabled && condition(e)
        val value = description(in.findElement(by))
        context.remember(result, value)
        result
      },
      description(in.findElement(by)),
      action(in.findElement(by))
    )
  }
}
