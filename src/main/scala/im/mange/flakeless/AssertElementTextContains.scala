package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, Intention, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementTextContains {
  def apply(flakeless: Flakeless, by: By, expected: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  def apply(in: WebElement, by: By, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    val intention = Intention("AssertElementTextContains", in, by, expected = Some(expected))

    WaitForElement(flakeless, intention,

      description = e => {
        Description(actual = Some((e) => e.getText))
          .describeActual(e)
      },

      condition = e => e.getText.contains(expected))
  }
}