package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}
import scala.collection.JavaConverters._


private [flakeless] object WaitForElements {

  def apply(flakeless: Option[Flakeless], intention: Intention,
            description: (List[WebElement]) => String,
            condition: (List[WebElement]) => Boolean) = {

    Execute(flakeless, new WaitForElements(intention, description, condition))
  }
}

private class WaitForElements(val intention: Intention,
            description: (List[WebElement]) => String,
            condition: (List[WebElement]) => Boolean) extends Executable {

  override def execute(context: Context) {
    Wait.waitUpTo().forCondition(
      {
        val result = condition(intention.in.findElements(intention.by).asScala.toList)
        val value = description(intention.in.findElements(intention.by).asScala.toList)
        context.remember(result, value)
        result
      },
      description(intention.in.findElements(intention.by).asScala.toList)
    )
  }
}
