package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementDisplayedness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementDisplayed {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementDisplayedness(Body(flakeless.rawWebDriver), by, expected = true, Some(flakeless))
  }

  def apply(in: WebElement, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementDisplayedness(in, by, expected = true, flakeless)
  }
}
