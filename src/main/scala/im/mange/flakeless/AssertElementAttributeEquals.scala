package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementAttributeEquals {
  def apply(webDriver: WebDriver, by: By, attribute: String, expected: String): Unit = {
    apply(Body(webDriver), by, attribute, expected)
  }

  def apply(in: WebElement, by: By, attribute: String, expected: String): Unit = {
    WaitForElement(in, by,

      description = e =>
        Description("AssertElementAttributeEquals", in, by, args = Map("attribute" -> attribute), expected = Some(expected),
          actual = Some((e) => e.getAttribute(attribute)))
          .describe(e),

      condition = e => e.getAttribute(attribute) == expected)
  }
}
