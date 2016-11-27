package im.mange.flakeless

import im.mange.flakeless.innards.{Body, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementAttributeEquals {
  def apply(webDriver: WebDriver, by: By, attribute: String, expected: String): Unit = {
    apply(Body(webDriver), by, attribute, expected)
  }

  def apply(in: WebElement, by: By, attribute: String, expected: String): Unit = {
    WaitForElement(in, by,
      description = e => s"AssertElementAttributeEquals\n| in: $in\n| $by\n| attribute: '$attribute'\n| expected: '$expected'\n| but was: '${e.getAttribute(attribute)}'",
      condition = e => e.getAttribute(attribute) == expected)
  }
}
