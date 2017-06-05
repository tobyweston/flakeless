package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Intention, WaitForElements}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementListTextContains {
  def apply(flakeless: Flakeless, by: By, expected: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  //TODO: I need to be converted to a Description, just not possible yet..
  def apply(in: WebElement, by: By, expected: String, flakeless: Option[Flakeless] = None): Unit = {
    val intention = Intention("AssertElementListTextContains", in, by, expected = Some(expected))

    WaitForElements(flakeless, intention,

      description = es => s"${es.map(t => s"'${t.getText}'").mkString(", ")}",

      condition = es => es.map(_.getText).contains(expected))
  }
}
