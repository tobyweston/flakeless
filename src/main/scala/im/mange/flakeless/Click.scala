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
  // s"AssertElementAttributeEquals\n| in: $in\n| $by\n| attribute: '$attribute'\n| expected: '$expected'\n| but was: '${e.getAttribute(attribute)}'",
  println(Description("AssertElementAttributeEquals", By.id("x"), Map("attribute" -> "class"), Some("moo")).describe)

}

case class Value(label: Option[String], value: String) {
  def describe = label match {
    case Some(l) => s"$l: '$value'"
    case None => value
  }
}

//TODO: add in, add butWas, render args
//TODO: this all needs to be jsonated later
case class Description(command: String, by: By, args: Map[String, String] = Map.empty, expected: Option[String] = None) {
  def describe = (Seq(
    Some(Value(None, command)),
    //in
    Some(Value(Some("by"), by.toString))) ++
    args.map(kv => Some(Value(Some(kv._1), kv._2))) ++
    Seq(
    expected.map(e => Value(Some("expected"), e))
  )).flatten.map(_.describe).mkString("\n| ")
}