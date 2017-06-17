package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

object WaitForInteractableElement {
  def apply(flakeless: Option[Flakeless], command: Command,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) {

    Execute(flakeless, new WaitForInteractableElement(command, description, condition, action, mustBeDisplayed))
  }
}

private class WaitForInteractableElement(val command: Command,
                                         description: (WebElement) => String,
                                         condition: (WebElement) => Boolean = (e) => {true},
                                         action: (WebElement) => Unit,
                                         mustBeDisplayed: Boolean = true) extends Executable {

  def execute(context: Context) {
    command.in.foreach(in =>
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(command,
      {
        val e = in.findElement(command.by)
        val result = (if (mustBeDisplayed) e.isDisplayed else true) && e.isEnabled && condition(e)
        val value = description(in.findElement(command.by))
        if (result) context.succeeded()
        else context.failed(value)
        result
      },
      description(in.findElement(command.by)),
      action(in.findElement(command.by))
    )
    )
  }
}
