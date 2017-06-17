package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, Description, WaitForElement}
import org.openqa.selenium.{By, WebElement}

object AssertElementAttributeEquals {
  def apply(flakeless: Flakeless, by: By, attribute: String, expected: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, attribute, expected, Some(flakeless))
  }

  def apply(in: WebElement, by: By, attribute: String, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    WaitForElement(flakeless,
      Command("AssertElementAttributeEquals", Some(in), by, args = Map("attribute" -> attribute), expected = Some(expected)),
      description = e => Description(actual = Some((e) => e.getAttribute(attribute))).describeActual(e),
      condition = e => e.getAttribute(attribute) == expected)
  }
}
