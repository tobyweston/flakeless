package im.mange.flakeless

import im.mange.flakeless.innards.{Body, WaitForElements}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementCountEquals {
  def apply(webDriver: WebDriver, by: By, expected: Int): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: Int): Unit = {
    WaitForElements(in, by,
      description = e => s"AssertElementCountEquals\n| in: $in\n| $by\n| expected: '$expected'\n| but was: '${e.size}'",
      condition = e => e.size == expected)
  }
}
