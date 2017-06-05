package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

object WaitForInteractableElement {
  def apply(flakeless: Option[Flakeless], intention: Command,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) {

    Execute(flakeless,
      new WaitForInteractableElement(intention, description, condition, action, mustBeDisplayed))
  }
}

private class WaitForInteractableElement(val command: Command,
                                         description: (WebElement) => String,
                                         condition: (WebElement) => Boolean = (e) => {true},
                                         action: (WebElement) => Unit,
                                         mustBeDisplayed: Boolean = true) extends Executable {

  def execute(context: Context) {
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(command,
      {
        val e = command.in.findElement(command.by)
        val result = (if (mustBeDisplayed) e.isDisplayed else true) && e.isEnabled && condition(e)
        val value = description(command.in.findElement(command.by))
        context.remember(result, value)
        result
      },
      description(command.in.findElement(command.by)),
      action(command.in.findElement(command.by))
    )
  }
}
