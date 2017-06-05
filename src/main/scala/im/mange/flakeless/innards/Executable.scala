package im.mange.flakeless.innards

import org.openqa.selenium.{By, WebElement}

trait Executable {
  def execute(context: Context)
  val intention: Intention
}

case class Intention(command: String, in: WebElement, by: By,
                     args: Map[String, String] = Map.empty,
                     expected: Option[String] = None,
                     expectedMany: Option[List[String]] = None) {
  def describe = s"$command $in $by $args $expected $expectedMany"
}
