package im.mange.flakeless

import im.mange.flakeless.innards.{Body, WaitForElements}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementListTextEquals {
  def apply(flakeless: Flakeless, by: By, expected: List[String]): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  //TODO: I need to be converted to a Description, just not possible yet..
  def apply(in: WebElement, by: By, expected: List[String], flakeless: Option[Flakeless] = None): Unit = {
    WaitForElements(in, by,

      description = es => s"AssertElementListTextEquals\n| in: $in\n| $by\n| expected: '$expected'\n| but was: '${es.map(t => s"'${t.getText}'").mkString(", ")}'",

      condition = es => es.map(_.getText) == expected)
  }
}
