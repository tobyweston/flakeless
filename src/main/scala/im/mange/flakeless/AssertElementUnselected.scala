package im.mange.flakeless

import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementUnselected {
  def apply(webDriver: WebDriver, by: By): Unit = {
    AssertElementSelectedness(Body(webDriver), by, expected = false)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementSelectedness(in, by, expected = false)
  }
}
