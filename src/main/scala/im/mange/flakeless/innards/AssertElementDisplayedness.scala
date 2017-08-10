package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, SearchContext}

private [flakeless] object AssertElementDisplayedness {
  def apply(in: SearchContext, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    WaitForElement(flakeless,
      Command(s"AssertElement${if (expected) "Displayed" else "Hidden"}", Some(in), Some(by)),
      description = e => Description(actual = Some((e) => if (e.isDisplayed) "displayed" else "hidden")).describeActual(e),
      condition = e => e.isDisplayed == expected)
  }
}
