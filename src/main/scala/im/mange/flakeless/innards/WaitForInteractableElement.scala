package im.mange.flakeless.innards

import im.mange.flakeless.{Context, Flakeless}
import org.openqa.selenium.{By, WebElement}

object WaitForInteractableElement {
  def apply(flakeless: Option[Flakeless], in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) {

    new WaitForInteractableElement(Context(), in, by, description, condition, action, mustBeDisplayed)
      .execute(flakeless)
  }
}

private class WaitForInteractableElement(context: Context, in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) {

  def execute(flakeless: Option[Flakeless]) {
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(
      {
        val e = in.findElement(by)
        val result = (if (mustBeDisplayed) e.isDisplayed else true) && e.isEnabled && condition(e)
        val value = description(in.findElement(by))

        flakeless.foreach(_.record(result, value))
        context.remember(result, value)
        result
      },
      description(in.findElement(by)),
      action(in.findElement(by))
    )
  }
}
