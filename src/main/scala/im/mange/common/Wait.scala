package im.mange.common

import scala.annotation.tailrec
import im.mange.driveby.DriveByConfig
import org.openqa.selenium.{By, WebElement}


private object WaitForInteractableElement {
  def apply(in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) = {

    //TODO: we should check there is only one element
    Wait.waitUpTo().forCondition(
      {
        val e = in.findElement(by)
        (if (mustBeDisplayed) e.isDisplayed else true) && e.isEnabled && condition(e)
      },
      description(in.findElement(by)),
      action(in.findElement(by))
    )
  }
}


private object WaitForElement {
  def apply(in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) = {

    //TODO: we should check there is only one element
    Wait.waitUpTo().forCondition(
      condition(in.findElement(by)),
      description(in.findElement(by))
    )
  }
}


private object WaitForElements {
  import scala.collection.JavaConverters._

  def apply(in: WebElement, by: By,
            description: (List[WebElement]) => String,
            condition: (List[WebElement]) => Boolean) = {

    Wait.waitUpTo().forCondition(
      condition(in.findElements(by).asScala.toList),
      description(in.findElements(by).asScala.toList)
    )
  }
}

object Wait {
  def waitUpTo(timeout: Long = DriveByConfig.waitTimeout, pollPeriod: Long = DriveByConfig.waitPollPeriod) = new Wait(timeout, pollPeriod)
}

private[mange] class Wait(timeout: Long, pollPeriod: Long) {
  def forCondition(f: => Boolean, desc: => String, action: => Unit = {}) {
    if (!conditionSatisfied(f, pollPeriod)) {
      throw new ConditionNotMetException("> FAILED: " + desc, timeout)
    } else {
      action
    }
  }

  private def conditionSatisfied[T](f: => Boolean, pollPeriod: Long): Boolean = {
    val end_? = System.currentTimeMillis() + timeout
    val end = if (end_? > 0) end_? else Long.MaxValue

    def tryIt = try {f} catch {case _: Exception => false}

    def timedOut = System.currentTimeMillis() >= end

    @tailrec
    def loop(last: Boolean): Boolean = {
      if (last || timedOut) last
      else {
        Thread.sleep(pollPeriod)
        loop(tryIt)
      }
    }
    loop(tryIt)
  }
}

class ConditionNotMetException(message: String) extends RuntimeException(message) {
  def this(conditionToCheck: String, millis: Long) = this(conditionToCheck + " (not met within " + millis + " millis)")
}