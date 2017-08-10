package im.mange.flakeless

import im.mange.flakeless.innards.{Command, WaitForElements}
import org.openqa.selenium.{By, SearchContext}

object AssertElementListCountEquals {
  def apply(flakeless: Flakeless, by: By, expected: Int): Unit = {
    apply(flakeless.rawWebDriver, by, expected, Some(flakeless))
  }

  //TODO: I need to be converted to a Description, just not possible yet
  def apply(in: SearchContext, by: By, expected: Int, flakeless: Option[Flakeless] = None): Unit = {
    WaitForElements(flakeless,
      Command("AssertElementListCountEquals", Some(in), Some(by), expected = Some(expected.toString)),
      description = es => s"${es.size}",
      condition = es => es.size == expected)
  }
}
