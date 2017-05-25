package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementAbleness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementEnabled {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementAbleness(Body(flakeless.rawWebDriver), by, expected = true, Some(flakeless))
  }

  def apply(in: WebElement, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementAbleness(in, by, expected = true, flakeless)
  }
}
