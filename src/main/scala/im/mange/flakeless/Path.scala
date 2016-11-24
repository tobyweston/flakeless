package im.mange.flakeless

import java.util

import org.openqa.selenium.{By, SearchContext, WebElement}

case class Path(bys: By*) extends By {
  import scala.collection.JavaConverters._

  override def findElements(context: SearchContext): util.List[WebElement] = {
    val r = bys match {
      case Nil => throw new RuntimeException("Path must contain at least one by")
      case headBy :: remainingBys => findNext(context.findElements(headBy).asScala.toList, remainingBys)
    }

    r.asJava

//    WaitForElement()
//    context.findElements(bys.head)
//    List.empty[WebElement].asJavaCollection
  }

  private def findNext(ins: List[WebElement], bys: List[By]): List[WebElement] = {
    bys match {
      case Nil => ins
      case headBy :: remainingBys => {
        if (ins.length != 1) throw new RuntimeException("by found multiple")
        else findNext(ins.head.findElements(headBy).asScala.toList, remainingBys)
      }
    }
  }
}

