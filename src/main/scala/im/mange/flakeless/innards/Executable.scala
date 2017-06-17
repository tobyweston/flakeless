package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebElement}

trait Executable {
  def execute(context: Context)
  val command: Command
}

//TODO: improve rendering of options and in etc ...
//TODO: args at end? expected's earlier?
case class Command(name: String, in: Option[WebElement], by: Option[By],
                   args: Map[String, String] = Map.empty,
                   expected: Option[String] = None,
                   expectedMany: Option[List[String]] = None) {

  //TODO: ultimately shouldn't need this here, extract formatter
  case class LabelAndValue(label: Option[String], value: String) {
    def describe = label match {
      case Some(l) => s"$l: '$value'"
      case None => value
    }
  }

  def describe = {
    (
      Seq(
        Some(LabelAndValue(None, name)),
        Some(by).map(b => LabelAndValue(Some("by"), b.toString)),
        in.map(i => LabelAndValue(Some("in"), i.toString))
      ) ++
        args.map(kv => Some(LabelAndValue(Some(kv._1), kv._2))) ++
        Seq(
          expected.map(e => LabelAndValue(Some("expected"), e)),
          expectedMany.map(e => LabelAndValue(Some("expectedMany"), e.mkString(", ")))
        )
      ).flatten.map(_.describe).mkString("\n| ")
  }
}