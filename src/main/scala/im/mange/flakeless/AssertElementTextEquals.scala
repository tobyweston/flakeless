package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementTextEquals {
  def apply(flakeless: Flakeless, by: By, expected: String): Unit = {
    apply(Body(flakeless.webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: String): Unit = {
    WaitForElement(in, by,

      description = e => Description("AssertElementTextEquals", in, by, expected = Some(expected),
        actual = Some((e) => e.getText))
        .describe(e),

      condition = e => e.getText == expected)
  }
}