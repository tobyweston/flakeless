package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebDriver}

object Body {
  //TODO: shouldn't this be polling too?!
  //TODO: in-fact it should probably be a Path!!
  def apply(webDriver: WebDriver) = webDriver.findElement(By.tagName("body"))
}
