package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementDisplayedness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementHidden {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementDisplayedness(Body(flakeless.rawWebDriver), by, expected = false, Some(flakeless))
  }

  def apply(in: WebElement, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementDisplayedness(in, by, expected = false, flakeless)
  }
}
