package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebElement}

object WaitForInteractableElement {
  def apply(in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) = {

    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(
      {
        val e = in.findElement(by)
        (if (mustBeDisplayed) e.isDisplayed else true) && e.isEnabled && condition(e)
      },
      description(in.findElement(by)),
      action(in.findElement(by))
    )
  }
}
