package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, WaitForElements}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementListTextEquals {
  def apply(flakeless: Flakeless, by: By, expected: List[String]): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  //TODO: I need to be converted to a Description, just not possible yet..
  def apply(in: WebElement, by: By, expected: List[String], flakeless: Option[Flakeless] = None): Unit = {
    WaitForElements(flakeless,
      Command("AssertElementListTextEquals", Some(in), by, expectedMany = Some(expected)),

      //TODO: this produces strange output i.e.
      //expected = 'List(Foo)' vs
      //actual = ''Foo2''
      description = es => s"${es.map(t => s"'${t.getText}'").mkString(", ")}",
      condition = es => es.map(_.getText) == expected)
  }
}
