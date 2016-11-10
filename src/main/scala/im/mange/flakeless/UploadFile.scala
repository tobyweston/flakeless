package im.mange.flakeless

import org.openqa.selenium.{By, WebElement}

/**
  * Created by pall on 10/11/2016.
  */
object UploadFile {
  def apply(in: WebElement, by: By, filename: String): Unit = {
    WaitForInteractableElement(in, by,
      description = e => s"UploadFile\n:| in: $in\n| $by\n| $filename",
      action = e => e.sendKeys(filename),
      mustBeDisplayed = false
    )
  }
}
