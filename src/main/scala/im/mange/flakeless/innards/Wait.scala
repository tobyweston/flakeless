package im.mange.flakeless.innards

import im.mange.driveby.DriveByConfig
import org.openqa.selenium.{By, WebDriver, WebElement}

import scala.annotation.tailrec






private [flakeless] object WaitForElements {
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

private object Wait {
  def waitUpTo(timeout: Long = DriveByConfig.waitTimeout, pollPeriod: Long = DriveByConfig.waitPollPeriod) = new Wait(timeout, pollPeriod)
}

private class Wait(timeout: Long, pollPeriod: Long) {
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



