package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebElement}

//TODO: feels like I should be a factory for a private class ... see: Click
object UploadFile {
  def apply(flakeless: Flakeless, by: By, filename: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, filename, Some(flakeless))
  }

  def apply(in: WebElement, by: By, filename: String, flakeless: Option[Flakeless] = None): Unit = {
    WaitForInteractableElement(flakeless,
      Command("UploadFile", Some(in), by, args = Map("filename" -> filename)),
      description = e => Description().describeActual(e),
      action = e => e.sendKeys(filename),
      mustBeDisplayed = false
    )
  }
}
