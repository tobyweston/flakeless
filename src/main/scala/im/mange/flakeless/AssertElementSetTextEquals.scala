package im.mange.flakeless

import im.mange.flakeless.innards.{Body, WaitForElements}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementSetTextEquals {
  def apply(flakeless: Flakeless, by: By, expected: Set[String]): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  //TODO: I need to be converted to a Description, just not possible yet..
  def apply(in: WebElement, by: By, expected: Set[String], flakeless: Option[Flakeless] = None): Unit = {
    WaitForElements(in, by,

      description = es => s"AssertElementSetTextEquals\n| in: $in\n| $by\n| expected: '$expected'\n| but was: '${es.map(t => s"'${t.getText}'").mkString(", ")}'",

      condition = es => es.map(_.getText).toSet == expected)
  }
}
