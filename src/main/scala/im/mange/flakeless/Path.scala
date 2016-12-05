package im.mange.flakeless

import java.util

import im.mange.flakeless.innards.PathException
import org.openqa.selenium.{By, SearchContext, WebElement}

object Path {
  var debug = false
}


case class Path(private val bys: By*) extends By {
  import scala.collection.JavaConverters._

  def extend(by: By) = {
    Path(bys.toList :+ by:_*)
  }

  override def findElements(context: SearchContext): util.List[WebElement] = {
    if (Path.debug) println(s"> ${toString} going to find Path in '$context'")

    try {
      val r = bys.toList match {
        case Nil => throw new PathException("Path must contain at least one By")
        case headBy :: remainingBys => findNext(context.findElements(headBy).asScala.toList, remainingBys, headBy)
      }

      val x = r.asJava
      if (Path.debug) println(s"> [${bys.toList.mkString(" -> ")}] in '$context' yields: [${x}]\n")
      x
    }
    catch {
      case e: Exception => throw new NoSuchElementException("Unable to find Path because " + e.getMessage)
    }
  }

  override def toString = {
    s"Path: [${bys.toList.mkString(" -> ")}]"
  }

  private def findNext(ins: List[WebElement], remainingBys: List[By], current: By): List[WebElement] = {
    if (Path.debug) println(s"> ${toString} findNext $current, remainder: [${remainingBys.mkString(" -> ")}]")

    remainingBys match {
      case Nil => ins
      case headBy :: tailsBys => {
        if (ins.length != 1) throw new PathException(
          s"'${toString}' should resolve to a single element at each level\n| but I found ${ins.length} elements for '${current}':\n| $ins\n"
        )
        else findNext(ins.head.findElements(headBy).asScala.toList, tailsBys, headBy)
      }
    }
  }
}

