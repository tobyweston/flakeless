package im.mange.flakeless.innards

import im.mange.flakeless.Flakeless
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

  override def execute(context: Context) {
    (command.in, command.by) match {
      case (Some(in), Some(by)) =>
        Wait.waitUpTo().forCondition(command, {
          val result = condition(in.findElements(by).asScala.toList)
          val value = description(in.findElements(by).asScala.toList)
          if (result) context.succeeded()
          else context.failed(value)
          result
        },
          description(in.findElements(by).asScala.toList)
        )
      case _ => throw new RuntimeException("cannot wait without in and by")
    }
  }
}
