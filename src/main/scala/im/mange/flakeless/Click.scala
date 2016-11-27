package im.mange.flakeless

import im.mange.flakeless.innards.{Body, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object Click {
  def apply(webDriver: WebDriver, by: By): Unit = {
    apply(Body(webDriver), by)
  }

  def apply(in: WebElement, by: By): Unit = {
    WaitForInteractableElement(in, by,
      description = e => s"Click\n| in: $in\n| $by",
      action = e => e.click()
    )
  }
}


object Moo extends App {
//  println(Click(new WebDriver {}))
  println(Description("Click", By.id("x")).describe)
}

case class Description(command: String, by: By) {
  def describe = Seq(Some(command), Some(by)).map(_.getOrElse("")).mkString("\n| ")
}