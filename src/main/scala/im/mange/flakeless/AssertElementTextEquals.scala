package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, Intention, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementTextEquals {
  def apply(flakeless: Flakeless, by: By, expected: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  def apply(in: WebElement, by: By, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    val intention = Intention("AssertElementTextEquals", in, by, expected = Some(expected))

    WaitForElement(flakeless, intention,

      description = e => {
        Description(intention,
          actual = Some((e) => e.getText))
          .describeActual(e)
      },

      condition = e => e.getText == expected)
  }
}