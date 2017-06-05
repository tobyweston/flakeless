package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, Intention, WaitForElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementAttributeContains {
  def apply(flakeless: Flakeless, by: By, attribute: String, expected: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, attribute, expected, Some(flakeless))
  }

  def apply(in: WebElement, by: By, attribute: String, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    val intention = Intention("AssertElementAttributeContains", in, by, args = Map("attribute" -> attribute), expected = Some(expected))

    WaitForElement(flakeless, intention,
      description = e => {
        Description(actual = Some((e) => e.getAttribute(attribute)))
          .describeActual(e)
      },

      condition = e => e.getAttribute(attribute).contains(expected))
  }
}
