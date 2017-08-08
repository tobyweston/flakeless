package im.mange.flakeless.innards

import im.mange.flakeless.{Config, Flakeless}
import org.openqa.selenium.{By, WebElement}

import scala.collection.JavaConverters._


private [flakeless] object WaitForElements {
  def apply(flakeless: Option[Flakeless], command: Command,
            description: (List[WebElement]) => String,
            condition: (List[WebElement]) => Boolean) = {

    Execute(flakeless, new WaitForElements(command, description, condition))
  }
}

private class WaitForElements(val command: Command,
                              description: (List[WebElement]) => String,
                              condition: (List[WebElement]) => Boolean) extends Executable {

  override def execute(context: Context, config: Config) {
    (command.in, command.by) match {
      case (Some(in), Some(by)) =>
        Wait.waitUpTo(config).forCondition(command, {
          val elements = in.findElements(by).asScala.toList
          val result = condition(elements)
          if (result) context.succeeded()
          else context.failed(description(elements))
          result
        },
          description(in.findElements(by).asScala.toList)
        )
      case _ => throw new RuntimeException("cannot wait without in and by")
    }
  }
}
