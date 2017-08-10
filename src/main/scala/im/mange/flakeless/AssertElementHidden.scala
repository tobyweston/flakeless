package im.mange.flakeless

import im.mange.flakeless.innards.AssertElementDisplayedness
import org.openqa.selenium.{By, SearchContext}

object AssertElementHidden {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementDisplayedness(flakeless.rawWebDriver, by, expected = false, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementDisplayedness(in, by, expected = false, flakeless)
  }
}
