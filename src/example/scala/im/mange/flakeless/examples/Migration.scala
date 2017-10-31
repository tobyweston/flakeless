package im.mange.flakeless.examples

import im.mange.flakeless.{AssertElementTextEquals, Click}
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver

object Migration extends App {
  val driver = new ChromeDriver()

  //For actions, replace:
  driver.findElement(By.id("container")).click()
  //with
  Click(driver, By.id("container"))

  //For assertions, replace
  driver.findElement(By.id("value")).getText == "expected"
  //with
  AssertElementTextEquals(driver, By.id("value"), "expected")

}
