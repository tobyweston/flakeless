package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, Intention, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementEmpty {
  def apply(flakeless: Flakeless, by: By): Unit = {
    apply(Body(flakeless.rawWebDriver), by, Some(flakeless))
  }

  def apply(in: WebElement, by: By, flakeless: Option[Flakeless] = None): Unit = {
    val intention = Intention("AssertElementEmpty", in, by, expected = Some(""))

    WaitForElement(flakeless, intention,

      description = e => {
        Description(intention,
          actual = Some((e) =>
            e.getText ++ " and " ++ e.findElements(By.xpath(".//*")).size.toString ++ " children "
          )
        ).describeActual(e)
      },

      condition = e => e.findElements(By.xpath(".//*")).size == 0 && e.getText.isEmpty)
  }
}
