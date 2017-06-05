package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, Intention, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object UploadFile {
  def apply(flakeless: Flakeless, by: By, filename: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, filename, Some(flakeless))
  }

  def apply(in: WebElement, by: By, filename: String, flakeless: Option[Flakeless] = None): Unit = {
    val intention = Intention("UploadFile", in, by, args = Map("filename" -> filename))

    WaitForInteractableElement(flakeless, intention,

      description = e => {
        Description().describeActual(e)
      },

      action = e => e.sendKeys(filename),

      mustBeDisplayed = false
    )
  }
}
