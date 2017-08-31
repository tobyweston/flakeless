package im.mange.flakeless.reporting.innards

import im.mange.flakeless.innards.{Command, Context}
import im.mange.flakeless.reporting.{AllFlightsReport, FlightReport}
import im.mange.flakeless.{Flakeless, FlightInvestigator, Path}
import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebElement

/**
  * Created by pall on 31/08/2017.
  */
object Example extends App {
  def go {
    val flakeless = Flakeless(null)
    flakeless.startFlight("Victor Vector")

    flakeless.inflightAnnouncement("announcement")
    flakeless.record(Command("command with expected", None, None, Map.empty, Some("expected")), Context())
    flakeless.record(Command("command with expected many", None, None, Map.empty, expectedMany = Some(List("expected", "expected2"))), Context())
    flakeless.record(Command("command with in", Some(createElement), None, Map.empty), Context())
    flakeless.record(Command("command with by", None, Some(By.id("id")), Map.empty), Context())
    flakeless.record(Command("command with by path", None, Some(Path(By.id("id"), By.className("class"))), Map.empty), Context())
    flakeless.record(Command("command with by path nested", None, Some(Path(By.id("id"), Path(By.className("class"), By.xpath("xpath")), By.cssSelector("cssSelector"))), Map.empty), Context())
    flakeless.record(Command("command with args", None, None, Map("key" -> "value")), Context())
    flakeless.record(Command("command with context true", None, None), Context(success = Some(true)))
    flakeless.record(Command("command with context false", None, None), Context(success = Some(false)))
    flakeless.record(Command("command with context failures", None, None), Context(List("failures")))

    //  if by's are a list then we are probably fine ... next Paths could be interesting .... (eek)
    flakeless.record(Command("everything", Some(createElement), Some(By.id("id")), Map("key" -> "value"), Some("expected"), Some(List("expected", "expected2"))), Context(List("failures"), success = Some(false)))
    flakeless.record(Command("everything with path", Some(createElement), Some(Path(By.id("id"))), Map("key" -> "value"), Some("expected"), Some(List("expected", "expected2"))), Context(List("failures"), success = Some(false)))

    flakeless.record(Command("escaping", Some(createElement), Some(By.id("id")), expectedMany = Some(List("\"expected"))), Context(List("failure \"1\"", "'2'"), success = Some(false)))

    //real world
    flakeless.record(Command("Goto", None, None, Map("url" -> "http://foo.bar.com/baz")), Context(Nil, success = Some(true)))
    flakeless.record(Command("Click", Some(createElement), Some(By.id("id"))), Context(List("failed for a bit"), success = Some(true)))
    flakeless.record(Command("AssertElementListTextEquals", Some(createElement), Some(By.id("id")), expectedMany = Some(List("expected"))), Context(List("failures"), success = Some(false)))

    flakeless.inflightAnnouncement("foo log", Some(List("line 1", "line 2", "line 3")))

    FlightReport(flakeless, "target/test-reports", captureImage = false, host = Some("http://localhost:63342/root"))

    flakeless.stopFlight()
    AllFlightsReport(flakeless, "target/test-reports")
  }

  def createElement: RemoteWebElement = {
    val element = new RemoteWebElement()
    element.setId("elementId")
    element
  }

  go
}
