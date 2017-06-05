package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

object WaitForInteractableElement {
  def apply(flakeless: Option[Flakeless], intention: Intention,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) {

    Execute(flakeless,
      new WaitForInteractableElement(intention, description, condition, action, mustBeDisplayed))
  }
}

private class WaitForInteractableElement(val intention: Intention,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) extends Executable {

  def execute(context: Context) {
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(
      {
        val e = intention.in.findElement(intention.by)
        val result = (if (mustBeDisplayed) e.isDisplayed else true) && e.isEnabled && condition(e)
        val value = description(intention.in.findElement(intention.by))
        context.remember(result, value)
        result
      },
      description(intention.in.findElement(intention.by)),
      action(intention.in.findElement(intention.by))
    )
  }
}
