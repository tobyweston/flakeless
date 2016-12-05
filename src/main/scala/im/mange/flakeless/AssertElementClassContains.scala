package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementClassContains {
  def apply(webDriver: WebDriver, by: By, expected: String): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: String): Unit = {
    val attribute = "class"

    WaitForElement(in, by,

      description = e =>
        Description("AssertElementClassContains", in, by, args = Map("attribute" -> attribute), expected = Some(expected),
          actual = Some((e) => e.getAttribute(attribute)))
          .describe(e),

      condition = e => e.getAttribute(attribute).contains(expected))
  }
}
