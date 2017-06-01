package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}
import scala.collection.JavaConverters._


private [flakeless] object WaitForElements {

  def apply(flakeless: Option[Flakeless], in: WebElement, by: By,
            description: (List[WebElement]) => String,
            condition: (List[WebElement]) => Boolean) = {

    Execute(flakeless, new WaitForElements(in, by, description, condition))
  }
}

private class WaitForElements(in: WebElement, by: By,
            description: (List[WebElement]) => String,
            condition: (List[WebElement]) => Boolean) extends Executable {

  override def execute(context: Context) {
    Wait.waitUpTo().forCondition(
      {
        val result = condition(in.findElements(by).asScala.toList)
        val value = description(in.findElements(by).asScala.toList)
        context.remember(result, value)
        result
      },
      description(in.findElements(by).asScala.toList)
    )
  }
}
