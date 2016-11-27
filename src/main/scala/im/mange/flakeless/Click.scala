package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, WebDriver, WebElement}

object Click {
  def apply(webDriver: WebDriver, by: By): Unit = {
    apply(Body(webDriver), by)
  }

  def apply(in: WebElement, by: By): Unit = {
    WaitForInteractableElement(in, by,
      description = e => Description("Click", in, by).describe(e),
      action = e => e.click()
    )
  }
}


object Moo extends App {
//  println(Click(new WebDriver {}))
  println(Description("Click", null, By.id("x")).describe(null))
  // s"AssertElementAttributeEquals\n| in: $in\n| $by\n| attribute: '$attribute'\n| expected: '$expected'\n| but was: '${e.getAttribute(attribute)}'",
  println(Description("AssertElementAttributeEquals", null, By.id("x"), Map("attribute" -> "class"), Some("moo"), Some((_) => "butwas")).describe(null))
  println(Description("AssertElementAttributeEquals", null, By.id("x"), Map("attribute" -> "class"), Some("moo"), Some((e) => e.getAttribute("moo"))).describe(null))

}

