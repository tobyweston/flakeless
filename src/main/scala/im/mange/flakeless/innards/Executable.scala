package im.mange.flakeless.innards

import im.mange.flakeless.{Config, Path}
import org.openqa.selenium.{By, WebElement}

trait Executable {
  def execute(context: Context, config: Config)
  val command: Command
}

//TODO: pull out more data about the in, tag, displayed etc
case class ReportCommand(name: String, in: Option[String], bys: Seq[By],
                   args: Map[String, String] = Map.empty,
                   expected: Option[String] = None,
                   expectedMany: Option[List[String]] = None)

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
        by.map(b => LabelAndValue(Some("by"), b.toString)),
        in.map(i => LabelAndValue(Some("in"), i.toString))
      ) ++
        args.map(kv => Some(LabelAndValue(Some(kv._1), kv._2))) ++
        Seq(
          expected.map(e => LabelAndValue(Some("expected"), e)),
          expectedMany.map(e => LabelAndValue(Some("expectedMany"), e.mkString(", ")))
        )
      ).flatten.map(_.describe).mkString("\n| ")
  }

  def report = {
    val inString = in.map(i => i.toString)

    def flat(by: By): Seq[By] = by match {
      case path: Path => path.bys.flatMap(flat)
      case b => Seq(b)
    }

    val bys = by.fold(Seq.empty[By])(flat)

    ReportCommand(name, inString, bys, args, expected, expectedMany)
  }
}