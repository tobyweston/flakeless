package im.mange.flakeless

import org.openqa.selenium.{By, WebDriver, WebElement}

object AssertElementTextEquals {
  def apply(webDriver: WebDriver, by: By, expected: String): Unit = {
    apply(Body(webDriver), by, expected)
  }

  def apply(in: WebElement, by: By, expected: String): Unit = {
    WaitForElement(in, by,
      description = e => s"AssertElementTextEquals\n| in: $in\n| $by\n| expected: '$expected'\n| but was: '${e.getText}'",
      condition = e => e.getText == expected)
  }
}

object AssertElementEnabled {
  def apply(webDriver: WebDriver, by: By): Unit = {
    AssertElementAbleness(Body(webDriver), by, expected = true)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementAbleness(in, by, expected = true)
  }
}

object AssertElementDisabled {
  def apply(webDriver: WebDriver, by: By): Unit = {
    AssertElementAbleness(Body(webDriver), by, expected = false)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementAbleness(in, by, expected = false)
  }
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

object AssertElementSelected {
  def apply(webDriver: WebDriver, by: By): Unit = {
    AssertElementSelectedness(Body(webDriver), by, expected = true)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementSelectedness(in, by, expected = true)
  }
}

object AssertElementUnselected {
  def apply(webDriver: WebDriver, by: By): Unit = {
    AssertElementSelectedness(Body(webDriver), by, expected = false)
  }

  def apply(in: WebElement, by: By): Unit = {
    AssertElementSelectedness(in, by, expected = false)
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
