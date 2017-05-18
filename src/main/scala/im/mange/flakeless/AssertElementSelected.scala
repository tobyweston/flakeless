package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementSelectedness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementSelected {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementSelectedness(Body(flakeless.webDriver), by, expected = true)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementSelectedness(in, by, expected = true)
  }
}
