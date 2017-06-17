package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, Description, WaitForElement}
import org.openqa.selenium.{By, WebElement}

object AssertElementEmpty {
  def apply(flakeless: Flakeless, by: By): Unit = {
    apply(Body(flakeless.rawWebDriver), by, Some(flakeless))
  }

  def apply(in: WebElement, by: By, flakeless: Option[Flakeless] = None): Unit = {
    WaitForElement(flakeless,
      Command("AssertElementEmpty", Some(in), by, expected = Some("")),

      description = e => Description(actual =
        Some((e) => e.getText ++ " and " ++ e.findElements(By.xpath(".//*")).size.toString ++ " children ")
      ).describeActual(e),

      condition = e => e.findElements(By.xpath(".//*")).size == 0 && e.getText.isEmpty)
  }
}
