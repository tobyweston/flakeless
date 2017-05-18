package im.mange.flakeless

import im.mange.flakeless.innards.{Body, WaitForElements}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementListCountEquals {
  def apply(flakeless: Flakeless, by: By, expected: Int): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected)
  }

  //TODO: I need to be converted to a Description, just not possible yet
  def apply(in: WebElement, by: By, expected: Int): Unit = {
    WaitForElements(in, by,

      description = es => s"AssertElementListCountEquals\n| in: $in\n| $by\n| expected: '$expected'\n| but was: '${es.size}'",

      condition = es => es.size == expected)
  }
}
