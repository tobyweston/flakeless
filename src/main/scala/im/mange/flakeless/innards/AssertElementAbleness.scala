package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebDriver, WebElement}

private [flakeless] object AssertElementAbleness {
  def apply(in: WebElement, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    WaitForElement(flakeless,
      Command(s"AssertElement${if (expected) "Enabled" else "Disabled"}", in, by),
      description = e => Description(actual = Some((e) => if (e.isEnabled) "enabled" else "disabled")).describeActual(e),
      condition = e => e.isEnabled == expected)
  }
}
