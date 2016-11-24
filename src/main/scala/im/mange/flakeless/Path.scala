package im.mange.flakeless

import java.util

import org.openqa.selenium.{By, SearchContext, WebElement}

case class Path(private val bys: By*) extends By {
  import scala.collection.JavaConverters._

  def extend(by: By) = {
    Path(bys.toList :+ by:_*)
  }

  override def findElements(context: SearchContext): util.List[WebElement] = {
    val r = bys.toList match {
      case Nil => throw new RuntimeException("Path must contain at least one By")
      case headBy :: remainingBys => findNext(context.findElements(headBy).asScala.toList, remainingBys, headBy)
    }

    r.asJava
  }

  private def findNext(ins: List[WebElement], remainingBys: List[By], current: By): List[WebElement] = {
    remainingBys match {
      case Nil => ins
      case headBy :: tailsBys => {
        if (ins.length != 1) throw new RuntimeException(
          s"Path '${bys.mkString(" -> ")}' should resolve to a single element at each level\n| but I found ${ins.length} elements for '${current}':\n| $ins\n"
        )
        else findNext(ins.head.findElements(headBy).asScala.toList, tailsBys, headBy)
      }
    }
  }
}

