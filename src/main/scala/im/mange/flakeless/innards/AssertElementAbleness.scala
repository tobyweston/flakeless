package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, SearchContext}

private [flakeless] object AssertElementAbleness {
  def apply(in: SearchContext, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    WaitForElement(flakeless,
      Command(s"AssertElement${if (expected) "Enabled" else "Disabled"}", Some(in), Some(by)),
      description = e => Description(actual = Some((e) => if (e.isEnabled) "enabled" else "disabled")).describeActual(e),
      condition = e => e.isEnabled == expected)
  }
}
