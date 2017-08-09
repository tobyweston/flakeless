package im.mange.flakeless.innards

import org.openqa.selenium.{SearchContext, WebDriver}

object Body {
  def apply(webDriver: WebDriver): SearchContext = webDriver
}
