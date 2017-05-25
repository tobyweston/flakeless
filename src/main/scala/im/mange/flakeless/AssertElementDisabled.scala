package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementAbleness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementDisabled {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementAbleness(Body(flakeless.rawWebDriver), by, expected = false, Some(flakeless))
  }

  def apply(in: WebElement, by: By, flakeless: Option[Flakeless] = None): Unit = {
    AssertElementAbleness(in, by, expected = false, flakeless)
  }
}
