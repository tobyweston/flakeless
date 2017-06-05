package im.mange.flakeless.innards

import im.mange.driveby.DriveByConfig
import im.mange.flakeless.{ConditionNotMetException, Flakeless}
import org.openqa.selenium.{By, WebElement}

import scala.annotation.tailrec

private object Wait {
  //TODO: move config into Flakeless (just create a default one if not passed in)
  def waitUpTo(timeout: Long = DriveByConfig.waitTimeout, pollPeriod: Long = DriveByConfig.waitPollPeriod) = new Wait(timeout, pollPeriod)
}

private class Wait(timeout: Long, pollPeriod: Long) {
  def forCondition(intention: Intention, f: => Boolean, desc: => String, action: => Unit = {}) {
    if (!conditionSatisfied(f, pollPeriod)) {
      throw new ConditionNotMetException("> FAILED: " + intention + "`\n| actual: " + desc, timeout)
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



