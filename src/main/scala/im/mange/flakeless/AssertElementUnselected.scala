package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementSelectedness, Body}
import org.openqa.selenium.{By, SearchContext}

object AssertElementUnselected {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementSelectedness(Body(flakeless.rawWebDriver), by, expected = false, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementSelectedness(in, by, expected = false, flakeless)
  }
}
