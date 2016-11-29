package im.mange.flakeless

import im.mange.flakeless.innards.{AssertElementDisplayedness, Body}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementHidden {
  def apply(webDriver: WebDriver, by: By): Unit = {
    AssertElementDisplayedness(Body(webDriver), by, expected = false)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementDisplayedness(in, by, expected = false)
  }
}
