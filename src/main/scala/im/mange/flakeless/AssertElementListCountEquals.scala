package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Intention, WaitForElements}
import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementListCountEquals {
  def apply(flakeless: Flakeless, by: By, expected: Int): Unit = {
    apply(Body(flakeless.rawWebDriver), by, expected, Some(flakeless))
  }

  //TODO: I need to be converted to a Description, just not possible yet
  def apply(in: WebElement, by: By, expected: Int, flakeless: Option[Flakeless] = None): Unit = {
    val intention = Intention("AssertElementListCountEquals", in, by, expected = Some(expected.toString))

    WaitForElements(flakeless, intention,

      description = es => s"$intention| but was: '${es.size}'",

      condition = es => es.size == expected)
  }
}
