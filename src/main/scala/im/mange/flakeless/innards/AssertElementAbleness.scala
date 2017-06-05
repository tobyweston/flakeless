package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebDriver, WebElement}

private [flakeless] object AssertElementAbleness {
  def apply(webDriver: WebDriver, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    apply(Body(webDriver), by, expected, flakeless)
  }

  def apply(in: WebElement, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    val intention = Intention(s"AssertElement${if (expected) "Enabled" else "Disabled"}", in, by)

    WaitForElement(flakeless, intention,

      description = e => {
        Description(actual = Some((e) => if (e.isEnabled) "enabled" else "disabled"))
          .describeActual(e)
      },

      condition = e => e.isEnabled == expected)
  }
}
