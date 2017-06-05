package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebElement}

//TODO: jsonate me later
case class Description(command: String, in: WebElement, by: By, args: Map[String, String] = Map.empty, expected: Option[String] = None, actual: Option[(WebElement) => String] = None) {
  case class LabelAndValue(label: Option[String], value: String) {
    def describe = label match {
      case Some(l) => s"$l: '$value'"
      case None => value
    }
  }

  def describeIntent = {
    try {
      reallyDescribe(None)
    }
    catch {
      case e: Exception => s"Exception thrown while describing $this cause:\n" + e.getMessage
    }
  }

  def describeActual(webElement: WebElement) = {
    try {
      reallyDescribe(Some(webElement))
    }
    catch {
      case e: Exception => s"Exception thrown while describing $this cause:\n" + e.getMessage
    }
  }

  private def reallyDescribe(webElement: Option[WebElement]): String = {
    (
      Seq(
        Some(LabelAndValue(None, command)),
        Some(LabelAndValue(Some("in"), in.toString)),
        Some(LabelAndValue(Some("by"), by.toString)),
        webElement.map(e => LabelAndValue(Some("displayed"), e.isDisplayed.toString)),
        webElement.map(e => LabelAndValue(Some("enabled"), e.isEnabled.toString))
      ) ++
        args.map(kv => Some(LabelAndValue(Some(kv._1), kv._2))) ++
        Seq(
          expected.map(e => LabelAndValue(Some("expected"), e)),
          actual.map(bw => LabelAndValue(Some("actual"), butWasSafely(webElement, bw)))
        )
      ).flatten.map(_.describe).mkString("\n| ")
  }

  private def butWasSafely(webElement: Option[WebElement], bw: (WebElement) => String): String = {
    try {
      webElement.fold("no element"){bw(_)}
    }
    catch {
      case e: Exception => "Exception thrown while getting actual: " + e.getMessage
    }
  }
}
