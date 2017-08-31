package im.mange.flakeless

import im.mange.flakeless.innards.AssertElementAbledness
import org.openqa.selenium.{By, SearchContext}

object AssertElementEnabled {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementAbledness(flakeless.rawWebDriver, by, expected = true, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementAbledness(in, by, expected = true, flakeless)
  }
}
