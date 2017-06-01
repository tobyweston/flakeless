package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

object WaitForElement {
  def apply(flakeless: Option[Flakeless],
            in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) = {

    Execute(flakeless, new WaitForElement(in, by, description, condition))
  }
}

private class WaitForElement(in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) extends Executable {

  override def execute(context: Context) {
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(
      {
        val result = condition(in.findElement(by))
        val value = description(in.findElement(by))
        context.remember(result, value)
        result
      },
      description(in.findElement(by))
    )
  }

}
