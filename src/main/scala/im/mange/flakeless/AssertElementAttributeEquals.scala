package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Description, WaitForElement}
import org.openqa.selenium.{By, SearchContext}

object AssertElementAttributeEquals {
  def apply(flakeless: Flakeless, by: By, attribute: String, expected: String): Unit = {
    apply(flakeless.rawWebDriver, by, attribute, expected, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, attribute: String, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    WaitForElement(flakeless,
      Command("AssertElementAttributeEquals", Some(in), Some(by), args = Map("attribute" -> attribute), expected = Some(expected)),
      description = e => Description(actual = Some((e) => e.getAttribute(attribute))).describeActual(e),
      condition = e => e.getAttribute(attribute) == expected)
  }
}
