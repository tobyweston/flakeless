package im.mange.flakeless.examples

import im.mange.flakeless.{AssertElementTextEquals, Click}
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver

object Migration extends App {
  val driverOrElement = new ChromeDriver()

  //(1) Replace side effecting actions on webElements e.g. .click(), .sendKeys() with corresponding primitive Click(), SendKeys()

      //e.g.
      driverOrElement.findElement(By.id("container")).click()
      //becomes
      Click(driverOrElement, By.id("container"))


  //(2) Replace all state querying on webElements with corresponding assertions

    //e.g.
    driverOrElement.findElement(By.id("value")).getText == "expected"
    //becomes
    AssertElementTextEquals(driverOrElement, By.id("value"), "expected")

  //(3) Repeat (1) and (2) until there are no usages of .findElement and .findElements

  //(4) Stop passing WebElements around they could be stale, always use driver

}
