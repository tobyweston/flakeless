package im.mange.flakeless

import im.mange.flakeless.innards.{Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebElement}

object UploadFile {
  def apply(in: WebElement, by: By, filename: String): Unit = {
    WaitForInteractableElement(in, by,

      description = e => Description("UploadFile", in, by, args = Map("filename" -> filename)).describe(e),

      action = e => e.sendKeys(filename),

      mustBeDisplayed = false
    )
  }
}
