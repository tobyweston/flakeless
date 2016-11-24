package im.mange.flakeless

import org.openqa.selenium.{By, WebDriver, WebElement}

trait FluentDriver {

  def assertElementAttributeContains(webDriver: WebDriver, by: By, attribute: String, expected: String): this.type = {
    AssertElementAttributeContains(webDriver, by, attribute, expected); this
  }

  def assertElementAttributeContains(webElement: WebElement, by: By, attribute: String, expected: String): this.type = {
    AssertElementAttributeContains(webElement, by, attribute, expected); this
  }

  def assertElementAttributeEquals(webDriver: WebDriver, by: By, attribute: String, expected: String): this.type = {
    AssertElementAttributeEquals(webDriver, by, attribute, expected); this
  }

  def assertElementAttributeEquals(webElement: WebElement, by: By, attribute: String, expected: String): this.type = {
    AssertElementAttributeEquals(webElement, by, attribute, expected); this
  }

  def assertElementCountEquals(webDriver: WebDriver, by: By, expected: Int): this.type = {
    AssertElementCountEquals(webDriver, by, expected); this
  }

  def assertElementCountEquals(webElement: WebElement, by: By, expected: Int): this.type = {
    AssertElementCountEquals(webElement, by, expected); this
  }
}
