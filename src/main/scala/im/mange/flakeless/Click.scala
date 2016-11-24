package im.mange.flakeless

import org.openqa.selenium.{By, WebDriver, WebElement}

object Click {
  def apply(webDriver: WebDriver, by: By): Unit = {
    apply(Body(webDriver), by)
  }

  def apply(in: WebElement, by: By): Unit = {
    WaitForInteractableElement(in, by,
      description = e => s"Click\n| in: $in\n| $by",
      action = e => e.click()
    )
  }
}
