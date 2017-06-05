package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

object WaitForElement {
  def apply(flakeless: Option[Flakeless],
            intention: Command,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) = {

    Execute(flakeless, new WaitForElement(intention, description, condition))
  }
}

private class WaitForElement(val command: Command,
                             description: (WebElement) => String,
                             condition: (WebElement) => Boolean) extends Executable {

  override def execute(context: Context) {
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(command,
      {
        val result = condition(command.in.findElement(command.by))
        val value = description(command.in.findElement(command.by))
        context.remember(result, value)
        result
      },
      description(command.in.findElement(command.by))
    )
  }

}
