package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Description, WaitForInteractableElement}
import org.openqa.selenium._

//TODO: feels like I should be a factory for a private class ... see: Click
object UploadFile {
  def apply(flakeless: Flakeless, by: By, filename: String): Unit = {
    apply(flakeless.rawWebDriver, by, filename, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, filename: String, flakeless: Option[Flakeless] = None): Unit = {
    WaitForInteractableElement(flakeless,
      Command("UploadFile", Some(in), Some(by), args = Map("filename" -> filename)),
      description = e => Description().describeActual(e),
      action = e => e.sendKeys(filename),
      mustBeDisplayed = false
    )
  }
}


