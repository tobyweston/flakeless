package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebDriver, WebElement}

private [flakeless] object AssertElementDisplayedness {
  def apply(webDriver: WebDriver, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    apply(Body(webDriver), by, expected, flakeless)
  }

  def apply(in: WebElement, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    WaitForElement(flakeless, in, by,

      description = e => Description(s"AssertElement${if (expected) "Displayed" else "Hidden"}", in, by,
        actual = Some((e) => if (e.isDisplayed) "displayed" else "hidden" ))
        .describe(e),

      condition = e => e.isDisplayed == expected)
  }
}
