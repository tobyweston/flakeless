package im.mange.flakeless

import im.mange.flakeless.innards.AssertElementAbleness
import org.openqa.selenium.{By, SearchContext}

object AssertElementEnabled {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementAbleness(flakeless.rawWebDriver, by, expected = true, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementAbleness(in, by, expected = true, flakeless)
  }
}
