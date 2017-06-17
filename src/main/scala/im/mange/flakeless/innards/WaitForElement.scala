package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

//TODO: model Condition
//TODO: work out when description is an Actual and when its something else (like not found)
//... Actual sounds assertion based
//... other sounds like an Action ...
//... in which case arr we in Action & Check extends Command land?

object WaitForElement {
  def apply(flakeless: Option[Flakeless], command: Command,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) = {

    Execute(flakeless, new WaitForElement(command, description, condition))
  }
}

private class WaitForElement(val command: Command,
                             description: (WebElement) => String,
                             condition: (WebElement) => Boolean) extends Executable {

  override def execute(context: Context) {
    command.in.foreach(in =>
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(command,
      {
        val result = condition(in.findElement(command.by))
        val value = description(in.findElement(command.by))
        if (result) context.succeeded()
        else context.failed(value)
        result
      },
      description(in.findElement(command.by))
    )
    )
  }

}
