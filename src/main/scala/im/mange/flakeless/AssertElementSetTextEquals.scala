package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Intention, WaitForElements}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementSetTextEquals {
  def apply(flakeless: Flakeless, by: By, expected: Set[String]): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  //TODO: I need to be converted to a Description, just not possible yet..
  def apply(in: WebElement, by: By, expected: Set[String], flakeless: Option[Flakeless] = None): Unit = {
    val intention = Intention("AssertElementSetTextEquals", in, by, expectedMany = Some(expected.toList))

    WaitForElements(flakeless, intention,

      description = es => s"$intention| but was: '${es.map(t => s"'${t.getText}'").mkString(", ")}'",

      condition = es => es.map(_.getText).toSet == expected)
  }
}
