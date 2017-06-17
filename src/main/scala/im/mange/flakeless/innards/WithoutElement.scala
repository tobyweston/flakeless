package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{WebDriver, WebElement}

object WithoutElement {
  def apply(flakeless: Flakeless, command: Command,
            action: (WebDriver) => Unit) {

    Execute(Some(flakeless), new WithoutElement(command, action, flakeless.rawWebDriver))
  }
}

private class WithoutElement(val command: Command,
                             action: (WebDriver) => Unit,
                             webDriver: WebDriver) extends Executable {

  def execute(context: Context) {
    action(webDriver)
  }
}