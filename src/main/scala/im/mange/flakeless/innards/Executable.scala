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
  def describe = s"$command $by $in $args $expected $expectedMany"
}
