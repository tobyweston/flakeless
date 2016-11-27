package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebElement}

object WaitForElement {
  def apply(in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) = {

    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(
      condition(in.findElement(by)),
      description(in.findElement(by))
    )
  }
}
