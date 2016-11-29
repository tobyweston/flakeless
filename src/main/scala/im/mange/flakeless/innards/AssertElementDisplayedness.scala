package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebDriver, WebElement}

private [flakeless] object AssertElementDisplayedness {
  def apply(webDriver: WebDriver, by: By, expected: Boolean): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: Boolean): Unit = {
    WaitForElement(in, by,

      description = e => Description(s"AssertElement${if (expected) "Displayed" else "Hidden"}", in, by,
        actual = Some((e) => if (e.isDisplayed) "displayed" else "hidden" ))
        .describe(e),

      condition = e => e.isDisplayed == expected)
  }
}
