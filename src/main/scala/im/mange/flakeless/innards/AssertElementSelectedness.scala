package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebDriver, WebElement}

private [flakeless] object AssertElementSelectedness {
  def apply(webDriver: WebDriver, by: By, expected: Boolean): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: Boolean): Unit = {
    WaitForElement(in, by,

      description = e => Description(s"AssertElement${if (expected) "Selected" else "Unselected"}", in, by,
        actual = Some((e) => if (e.isSelected) "selected" else "unselected"))
        .describe(e),

      condition = e => e.isSelected == expected)
  }
}
