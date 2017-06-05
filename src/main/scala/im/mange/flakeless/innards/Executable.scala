package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebElement}

trait Executable {
  def execute(context: Context)
  val intention: Intention
}

//TODO: probably rename to Command
//TODO: improve rendering of options and in etc ...
//TODO: args at end? expected's earlier?
case class Intention(command: String, in: WebElement, by: By,
                     args: Map[String, String] = Map.empty,
                     expected: Option[String] = None,
                     expectedMany: Option[List[String]] = None) {


//  def describe = s"$command $by $in $args $expected $expectedMany"

  //TODO: this is all well hokey
  def describe = reallyDescribe(None)

  //TODO: ultimately shouldn't need this here, extract formatter
  case class LabelAndValue(label: Option[String], value: String) {
    def describe = label match {
      case Some(l) => s"$l: '$value'"
      case None => value
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
          expected.map(e => LabelAndValue(Some("expected"), e))
        )
      ).flatten.map(_.describe).mkString("\n| ")
  }

}
