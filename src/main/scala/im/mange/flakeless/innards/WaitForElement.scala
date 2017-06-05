package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

object WaitForElement {
  def apply(flakeless: Option[Flakeless],
            intention: Intention,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) = {

    Execute(flakeless, new WaitForElement(intention, description, condition))
  }
}

private class WaitForElement(val intention: Intention,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) extends Executable {

  override def execute(context: Context) {
    //TODO: we should ensure there is only one element - make configurable
    Wait.waitUpTo().forCondition(intention,
      {
        val result = condition(intention.in.findElement(intention.by))
        val value = description(intention.in.findElement(intention.by))
        context.remember(result, value)
        result
      },
      description(intention.in.findElement(intention.by))
    )
  }

}
