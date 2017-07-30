package im.mange.flakeless.report

import im.mange.flakeless.innards.{Command, Context}
import im.mange.flakeless.{Flakeless, Path, report}
import org.openqa.selenium.remote.RemoteWebElement
import org.openqa.selenium.{By, OutputType, TakesScreenshot}


object Example extends App {
  def go {
    val flakeless = Flakeless(null)
    flakeless.newFlight()

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
    flakeless.record(Command("everything wiht path", Some(createElement), Some(Path(By.id("id"))), Map("key" -> "value"), Some("expected"), Some(List("expected", "expected2"))), Context(List("failures"), success = Some(false)))

    //real world
    flakeless.record(Command("Click", Some(createElement), Some(By.id("id"))), Context(Nil, success = Some(true)))
    flakeless.record(Command("AssertElementListTextEquals", Some(createElement), Some(By.id("id")), expectedMany = Some(List("expected"))), Context(List("failures"), success = Some(false)))

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
      val filepath = s"$outputDirectory/${"%04d".format(flakeless.getCurrentFlightNumber)}/"
      Files.createDirectories(Paths.get(filepath))

      val when = System.currentTimeMillis()
      val imagePath = path(filepath, s"$when.png")
      val htmlPath = path(filepath, s"flakeless.html")
//      val jsonPath = path(filepath, s"$when.json")
      val jsPath = path(filepath, s"flakeless.js")

      if (captureImage) write(imagePath, screenshot(flakeless))

      val data = flakeless.jsonFlightData()
      write(htmlPath, htmlContent(when, flakeless, data).getBytes)
//      write(jsonPath, data.getBytes)

      if (!jsPath.toFile.exists()) write(jsPath, report.Assets.flakelessJs.getBytes)

      System.err.println("*** Flakeless report: " + htmlPath.toAbsolutePath.toString)
    } catch {
      case t: Exception => System.err.println("*** Failed to write report something bad happened ***\n")
    }

    //TODO: might be able to kill the lozenge now ...
    def htmlContent(when: Long, flakeless: Flakeless, data: String) =
s"""
  |<html>
  |<head>
  |<style>
  |.lozenge {
  |  background-color: #fff;
  |  border: 1px solid #CCC;
  |  padding: 1px 4px;
  |  /*font-weight: bold;*/
  |
  |  -webkit-border-radius: 3px;
  |     -moz-border-radius: 3px;
  |          border-radius: 3px;
  |}
  |</style>
  |</head>
  |<body>
  |  <script type="text/javascript" src="flakeless.js"></script>
  |  <script>
  |    var data = '${data.replaceAll("\n", "")}';
  |    var app = Elm.Main.fullscreen()
  |    app.ports.data.send(data);
  |  </script>
  |</body>
  |</html>
""".stripMargin

  }

  private def write(path: Path, content: Array[Byte]) = Files.write(path, content)

  private def screenshot(flakeless: Flakeless) =
    flakeless.rawWebDriver.asInstanceOf[TakesScreenshot].getScreenshotAs(OutputType.BYTES)

  private def path(filepath: String, filename: String) = Paths.get(filepath + filename)
}
