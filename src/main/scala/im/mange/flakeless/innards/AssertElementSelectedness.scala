package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebDriver, WebElement}

private [flakeless] object AssertElementSelectedness {
  def apply(in: WebElement, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    WaitForElement(flakeless,
      Command(s"AssertElement${if (expected) "Selected" else "Unselected"}", in, by),
      description = e => Description(actual = Some((e) => if (e.isSelected) "selected" else "unselected")).describeActual(e),
      condition = e => e.isSelected == expected)
  }
}
