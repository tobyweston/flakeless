package im.mange.flakeless

import im.mange.flakeless.innards.{Command, Description, WaitForElement}
import org.openqa.selenium.{By, SearchContext}

object AssertElementClassEquals {
  def apply(flakeless: Flakeless, by: By, expected: String): Unit = {
    apply(flakeless.rawWebDriver, by, expected, Some(flakeless))
  }

  def apply(in: SearchContext, by: By, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    val attribute = "class"

    WaitForElement(flakeless,
      Command("AssertElementClassEquals", Some(in), Some(by), args = Map("attribute" -> attribute), expected = Some(expected)),
      description = e => Description(actual = Some((e) => e.getAttribute(attribute))).describeActual(e),
      condition = e => e.getAttribute(attribute) == expected)
  }
}
