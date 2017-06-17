package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{WebDriver, WebElement}

object JustDoIt {
  def apply(flakeless: Flakeless, command: Command,
            description: (WebElement) => String,
            action: (WebDriver) => Unit) {

    Execute(Some(flakeless), new JustDoIt(command, description, action, flakeless.rawWebDriver))
  }
}

private class JustDoIt(val command: Command,
                       description: (WebElement) => String,
                       action: (WebDriver) => Unit,
                       webDriver: WebDriver) extends Executable {

  def execute(context: Context) {
    action(webDriver)
  }
}