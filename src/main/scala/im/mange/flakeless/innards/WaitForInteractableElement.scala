package im.mange.flakeless.innards

import im.mange.flakeless.{Context, Flakeless}
import org.openqa.selenium.{By, WebElement}

object WaitForInteractableElement {
  def apply(flakeless: Option[Flakeless], in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) {

    Execute(flakeless,
      new WaitForInteractableElement(Context(), in, by, description, condition, action, mustBeDisplayed))
  }
}

trait Executable {
  def execute(flakeless: Option[Flakeless])
}

private class WaitForInteractableElement(context: Context, in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) extends Executable {

  def execute(flakeless: Option[Flakeless]) {
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(
      {
        val e = in.findElement(by)
        val result = (if (mustBeDisplayed) e.isDisplayed else true) && e.isEnabled && condition(e)
        val value = description(in.findElement(by))
        //TODO: ultimately don't do this
        flakeless.foreach(_.record(result, value))
        context.remember(result, value)
        result
      },
      description(in.findElement(by)),
      action(in.findElement(by))
    )
  }
}

object Execute {
  def apply(flakeless: Option[Flakeless], executable: Executable): Unit = {
    //TODO: probably should pass the context in here instead
    executable.execute(flakeless)
  }
}