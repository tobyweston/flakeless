package im.mange.flakeless.examples

import im.mange.flakeless._
import org.openqa.selenium.{By, WebDriver, WebElement}

object MigrationGuide extends App {
  //given
  val webDriver: WebDriver = ???
  val element: WebElement = ???


  //(1) Replace side effecting actions on webElements e.g. .click(), .sendKeys() with corresponding primitive Click(), SendKeys()

    //e.g.
    element.findElement(By.id("container")).click()
    //becomes
    Click(element, By.id("container"))

    //e.g.
    element.findElement(By.id("container")).sendKeys("foo")
    //becomes
    SendKeys(element, By.id("container"), List("foo"))


  //(2) Replace all state querying on webElements with corresponding assertions

    //e.g.
    element.findElement(By.id("value")).getText == "expected"
    //becomes
    AssertElementTextEquals(element, By.id("value"), "expected")

    //e.g.
    element.findElement(By.id("value")).getText.contains("contains")
    //becomes
    AssertElementTextContains(element, By.id("value"), "contains")


  //(3) Repeat (1) and (2) until there are no usages of .findElement and .findElements

  //(4) Stop holding onto webElements they could be stale, always start from webDriver, if nesting is required use a Path

    //e.g.
    val parentElement = element.findElement(By.id("parent"))
    val childElement = parentElement.findElement(By.id("child"))
    childElement.click()
    //becomes
    val parentPath = Path(By.id("parent"))
    Click(webDriver, parentPath.extend(By.id("child")))
    //or
    Click(webDriver, Path(By.id("parent"), By.id("child")))


  //(5) That's it!

}
