package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebElement}

private [flakeless] object WaitForElements {
  import scala.collection.JavaConverters._

  def apply(flakeless: Option[Flakeless], in: WebElement, by: By,
            description: (List[WebElement]) => String,
            condition: (List[WebElement]) => Boolean) = {

    Wait.waitUpTo().forCondition(
      {
        val result = condition(in.findElements(by).asScala.toList)
        flakeless.foreach(_.record(result, description(in.findElements(by).asScala.toList)))
        result
      },
      description(in.findElements(by).asScala.toList)
    )
  }
}
