package im.mange.flakeless.innards

import im.mange.flakeless.{ConditionNotMetException, Config}

import scala.annotation.tailrec

private object Wait {
  def waitUpTo(config: Config) = new Wait(config)
}

private class Wait(config: Config) {
  def forCondition(command: Command, f: => Boolean, desc: => String, action: => Unit = {}) {
    if (!conditionSatisfied(f, config.waitPollPeriod)) {
      throw new ConditionNotMetException("> FAILED: \n| " + command.describe + "\n| actual: " + desc + "\n", config.waitTimeout)
    } else {
      action
    }
  }

  private def conditionSatisfied[T](f: => Boolean, pollPeriod: Long): Boolean = {
    val end_? = System.currentTimeMillis() + config.waitTimeout
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



