package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebDriver, WebElement}

private [flakeless] object AssertElementAbleness {
  def apply(webDriver: WebDriver, by: By, expected: Boolean): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: Boolean): Unit = {
    WaitForElement(in, by,

      description = e => Description(s"AssertElement${if (expected) "Enabled" else "Disabled"}", in, by,
        actual = Some((e) => if (e.isEnabled) "enabled" else "disabled" ))
        .describe(e),

      condition = e => e.isEnabled == expected)
  }
}
