package im.mange.flakeless

import im.mange.flakeless.innards.AssertElementSelectedness
import org.openqa.selenium.{By, SearchContext}

object AssertElementSelected {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementSelectedness(flakeless.rawWebDriver, by, expected = true, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementSelectedness(in, by, expected = true, flakeless)
  }
}
