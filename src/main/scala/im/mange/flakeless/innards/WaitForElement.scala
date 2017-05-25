package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

object WaitForElement {
  def apply(flakeless: Option[Flakeless],
            in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) = {

    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(
      {
        val result = condition(in.findElement(by))
        flakeless.foreach(_.record(result, description(in.findElement(by))))
        result
      },
      description(in.findElement(by))
    )
  }
}
