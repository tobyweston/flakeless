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

  def describe(webElement: WebElement) = {
    try {
      reallyDescribe(webElement)
    }
    catch {
      case e: Exception => s"Exception thrown while describing $this cause:\n" + e.getMessage
    }
  }

  private def reallyDescribe(webElement: WebElement): String = {
    (
      Seq(
        Some(LabelAndValue(None, command)),
        Some(LabelAndValue(Some("in"), in.toString)),
        Some(LabelAndValue(Some("by"), by.toString))
      ) ++
        args.map(kv => Some(LabelAndValue(Some(kv._1), kv._2))) ++
        Seq(
          expected.map(e => LabelAndValue(Some("expected"), e)),
          actual.map(bw => LabelAndValue(Some("actual"), butWasSafely(webElement, bw)))
        )
      ).flatten.map(_.describe).mkString("\n| ")
  }

  private def butWasSafely(webElement: WebElement, bw: (WebElement) => String) = {
    try {
      bw(webElement)
    }
    catch {
      case e: Exception => "Exception thrown while getting actual: " + e.getMessage
    }
  }
}
