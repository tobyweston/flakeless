package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementAbleness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementDisabled {
  def apply(flakeless: Flakeless, by: By): Unit = {
    AssertElementAbleness(Body(flakeless.rawWebDriver), by, expected = false)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementAbleness(in, by, expected = false)
  }
}
