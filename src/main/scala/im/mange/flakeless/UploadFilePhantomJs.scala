package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForInteractableElement}
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.{By, WebDriver, WebElement}

object UploadFilePhantomJs {
  def apply(in: WebDriver, by: By, filename: String): Unit = {
    val webElement = Body(in)

    WaitForInteractableElement(webElement, by,

      description = e => Description("UploadFilePhantomJs", webElement, by, args = Map("filename" -> filename)).describe(e),

      action = e => {
        in.asInstanceOf[PhantomJSDriver].executePhantomJS(s"var page = this; page.uploadFile('$by', '$filename');")
      },

      mustBeDisplayed = false
    )
  }
}
