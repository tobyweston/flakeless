package im.mange.flakeless

import im.mange.flakeless.innards.WaitForInteractableElement
import org.openqa.selenium.{By, WebElement}

object UploadFile {
  def apply(in: WebElement, by: By, filename: String): Unit = {
    WaitForInteractableElement(in, by,
      description = e => s"UploadFile\n:| in: $in\n| $by\n| $filename",
      action = e => e.sendKeys(filename),
      mustBeDisplayed = false
    )
  }
}
