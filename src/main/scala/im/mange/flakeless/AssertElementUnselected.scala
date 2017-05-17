package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementSelectedness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementUnselected {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementSelectedness(Body(flakeless.driver), by, expected = false)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementSelectedness(in, by, expected = false)
  }
}
