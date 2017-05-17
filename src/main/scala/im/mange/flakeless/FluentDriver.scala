package im.mange.flakeless

import org.openqa.selenium.By

trait FluentDriver {
  val flakeless: Flakeless

  def assertElementAttributeContains(by: By, attribute: String, expected: String): this.type = {
    AssertElementAttributeContains(flakeless, by, attribute, expected); this
  }

  def assertElementAttributeEquals(by: By, attribute: String, expected: String): this.type = {
    AssertElementAttributeEquals(flakeless: Flakeless, by, attribute, expected); this
  }

  def assertElementListCountEquals(by: By, expected: Int): this.type = {
    AssertElementListCountEquals(flakeless: Flakeless, by, expected); this
  }
}
