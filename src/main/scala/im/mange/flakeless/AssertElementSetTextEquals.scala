package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, WaitForElements}
import org.openqa.selenium.{By, SearchContext}

object AssertElementSetTextEquals {
  def apply(flakeless: Flakeless, by: By, expected: Set[String]): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  //TODO: I need to be converted to a Description, just not possible yet..
  def apply(in: SearchContext, by: By, expected: Set[String], flakeless: Option[Flakeless] = None): Unit = {
    WaitForElements(flakeless,
      Command("AssertElementSetTextEquals", Some(in), Some(by), expectedMany = Some(expected.toList)),
      description = es => s"${es.map(t => s"""${t.getText}""").mkString(", ")}",
      condition = es => es.map(_.getText).toSet == expected)
  }
}
