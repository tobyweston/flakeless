package im.mange.flakeless

import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementTextEquals {
  def apply(webDriver: WebDriver, by: By, expected: String): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: String): Unit = {
    WaitForElement(in, by,
      description = e => s"AssertElementTextEquals\n| in: $in\n| $by\n| expected: '$expected'\n| but was: '${e.getText}'",
      condition = e => e.getText == expected)
  }
}
