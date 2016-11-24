package im.mange.flakeless

import im.mange.driveby.DriveByConfig
import org.openqa.selenium.{By, WebDriver, WebElement}

import scala.annotation.tailrec

object Body {
  //TODO: shouldn't this be polling too?!
  //TODO: in-fact it should probably be a Path!!
  def apply(webDriver: WebDriver) = webDriver.findElement(By.tagName("body"))
}

private object AssertElementAbleness {
  def apply(webDriver: WebDriver, by: By, expected: Boolean): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: Boolean): Unit = {
    WaitForElement(in, by,
      description = e => s"AssertElement${if (expected) "Enabled" else "Disabled"}\n| in: $in\n| $by\n| but was: '${if (e.isEnabled) "enabled" else "disabled"}'",
      condition = e => e.isEnabled == expected)
  }
}

private object AssertElementSelectedness {
  def apply(webDriver: WebDriver, by: By, expected: Boolean): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: Boolean): Unit = {
    WaitForElement(in, by,
      description = e => s"AssertElement${if (expected) "Selected" else "Unselected"}\n| in: $in\n| $by\n| but was: '${if (e.isSelected) "selected" else "unselected"}'",
      condition = e => e.isSelected == expected)
  }
}

object WaitForInteractableElement {
  def apply(in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean = (e) => {true},
            action: (WebElement) => Unit,
            mustBeDisplayed: Boolean = true) = {

    //TODO: we should check there is only one element - make configurable
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

object WaitForElement {
  def apply(in: WebElement, by: By,
            description: (WebElement) => String,
            condition: (WebElement) => Boolean) = {

    //TODO: we should check there is only one element - make configurable
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

private object Wait {
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

//TODO: rename this ...
class ConditionNotMetException(message: String) extends RuntimeException(message) {
  def this(conditionToCheck: String, millis: Long) = this(conditionToCheck + " (not met within " + millis + " millis)")
}

//TODO: rename this ...
class PathException(message: String) extends Exception(message)