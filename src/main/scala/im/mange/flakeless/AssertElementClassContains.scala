package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, Command, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementClassContains {
  def apply(flakeless: Flakeless, by: By, expected: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  def apply(in: WebElement, by: By, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    val attribute = "class"
    val intention = Command("AssertElementClassContains", in, by, args = Map("attribute" -> attribute), expected = Some(expected))

    WaitForElement(flakeless, intention,

      description = e => {
        Description(actual = Some((e) => e.getAttribute(attribute)))
          .describeActual(e)
      },

      condition = e => e.getAttribute(attribute).contains(expected))
  }
}
