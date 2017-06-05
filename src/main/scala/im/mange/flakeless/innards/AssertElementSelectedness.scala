package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
import org.openqa.selenium.{By, WebDriver, WebElement}

private [flakeless] object AssertElementSelectedness {
  def apply(webDriver: WebDriver, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    apply(Body(webDriver), by, expected, flakeless)
  }

  def apply(in: WebElement, by: By, expected: Boolean, flakeless: Option[Flakeless]): Unit = {
    val intention = Intention(s"AssertElement${if (expected) "Selected" else "Unselected"}", in, by)

    WaitForElement(flakeless, intention,

      description = e => {
        Description(actual = Some((e) => if (e.isSelected) "selected" else "unselected"))
          .describeActual(e)
      },

      condition = e => e.isSelected == expected)
  }
}
