package im.mange.flakeless

import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementSelected {
  def apply(webDriver: WebDriver, by: By): Unit = {
    AssertElementSelectedness(Body(webDriver), by, expected = true)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementSelectedness(in, by, expected = true)
  }
}
