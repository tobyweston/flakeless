package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object UploadFile {
  def apply(flakeless: Flakeless, by: By, filename: String): Unit = {
    apply(Body(flakeless.webDriver), by, filename)
  }

  def apply(in: WebElement, by: By, filename: String): Unit = {
    WaitForInteractableElement(in, by,

      description = e => Description("UploadFile", in, by, args = Map("filename" -> filename)).describe(e),

      action = e => e.sendKeys(filename),

      mustBeDisplayed = false
    )
  }
}
