package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementClassContains {
  def apply(flakeless: Flakeless, by: By, expected: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  def apply(in: WebElement, by: By, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    val attribute = "class"

    WaitForElement(flakeless, in, by,

      description = e =>
        Description("AssertElementClassContains", in, by, args = Map("attribute" -> attribute), expected = Some(expected),
          actual = Some((e) => e.getAttribute(attribute)))
          .describe(e),

      condition = e => e.getAttribute(attribute).contains(expected))
  }
}
