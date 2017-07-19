package im.mange.flakeless.report

import im.mange.flakeless.innards.{Command, Context}
import im.mange.flakeless.{Flakeless, Path, report}
import org.openqa.selenium.remote.RemoteWebElement
import org.openqa.selenium.{By, OutputType, TakesScreenshot}


object Example extends App {
  def go {
    val flakeless = Flakeless(null)
    flakeless.inflightAnnouncement("hello")
    flakeless.record(Command("command with expected", None, None, Map.empty, Some("expected")), Context())
    flakeless.record(Command("command with expected many", None, None, Map.empty, expectedMany = Some(List("expected", "expected2"))), Context())
    flakeless.record(Command("command with in", Some(createElement), None, Map.empty), Context())
    flakeless.record(Command("command with by", None, Some(By.id("id")), Map.empty), Context())
    flakeless.record(Command("command with by path", None, Some(Path(By.id("id"))), Map.empty), Context())
    flakeless.record(Command("command with args", None, None, Map("key" -> "value")), Context())
    flakeless.record(Command("command with context true", None, None), Context(success = Some(true)))
    flakeless.record(Command("command with context false", None, None), Context(success = Some(false)))
    flakeless.record(Command("command with context failures", None, None), Context(List("failures")))
    Report(flakeless, "target/test-reports", captureImage = false)
  }

  def createElement: RemoteWebElement = {
    val element = new RemoteWebElement()
    element.setId("elementId")
    element
  }

  go
}

object Report {
  import java.nio.file.{Files, Paths, Path}

  def apply(flakeless: Flakeless, outputDirectory: String, captureImage: Boolean = true) {

    try {
      val filepath = s"$outputDirectory/${"%04d".format(flakeless.currentFlightNumber)}/"
      Files.createDirectories(Paths.get(filepath))

      val when = System.currentTimeMillis()
      val imagePath = path(filepath, s"$when.png")
      val htmlPath = path(filepath, s"$when.html")
      val jsonPath = path(filepath, s"$when.json")
      val jsPath = path(filepath, s"flakeless.js")

      if (captureImage) write(imagePath, screenshot(flakeless))

      write(htmlPath, htmlContent(when, flakeless).getBytes)
      write(jsonPath, flakeless.jsonFlightData().getBytes)

      if (!jsPath.toFile.exists()) write(jsPath, report.Assets.flakelessJs.getBytes)

      System.err.println("*** Flakeless report: " + htmlPath.toAbsolutePath.toString)
    } catch {
      case t: Exception => System.err.println("*** Failed to write report something bad happened ***\n")
    }

    def htmlContent(when: Long, flakeless: Flakeless): String =
"""
  |<html>
  |<head>
  |</head>
  |<body>
  |  <script type="text/javascript" src="flakeless.js"></script>
  |  <script>
  |    var app = Elm.Main.fullscreen()
  |  </script>
  |</body>
  |</html>
  |
""".stripMargin

  }

  private def write(path: Path, content: Array[Byte]) = Files.write(path, content)

  private def screenshot(flakeless: Flakeless) =
    flakeless.rawWebDriver.asInstanceOf[TakesScreenshot].getScreenshotAs(OutputType.BYTES)

  private def path(filepath: String, filename: String) = Paths.get(filepath + filename)
}
