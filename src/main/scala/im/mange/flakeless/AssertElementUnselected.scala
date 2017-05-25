package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementSelectedness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementUnselected {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementSelectedness(Body(flakeless.rawWebDriver), by, expected = false, Some(flakeless))
  }

  def apply(in: WebElement, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementSelectedness(in, by, expected = false, flakeless)
  }
}
