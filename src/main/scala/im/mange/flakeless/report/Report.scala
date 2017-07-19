package im.mange.flakeless.report

import im.mange.flakeless.{Flakeless, report}
import org.openqa.selenium.{OutputType, TakesScreenshot}


object Example extends App {
  private val flakeless = Flakeless(null)
  flakeless.inflightAnnouncement("hello")
  Report(flakeless, "target/test-reports", captureImage = false)
}

object Report {
  import java.nio.file.{Files, Paths}

  def apply(flakeless: Flakeless, outputDirectory: String, captureImage: Boolean = true) {

    try {
      val filepath = s"$outputDirectory/${"%04d".format(flakeless.currentFlightNumber)}/"
      Files.createDirectories(Paths.get(filepath))

      val when = System.currentTimeMillis()
      val imagePath = Paths.get(filepath + s"$when.png")
      val htmlPath = Paths.get(filepath + s"$when.html")
      val jsonPath = Paths.get(filepath + s"$when.json")
      val jsPath = Paths.get(filepath + s"flakeless.js")

      if (captureImage) Files.write(imagePath, flakeless.rawWebDriver.asInstanceOf[TakesScreenshot].getScreenshotAs(OutputType.BYTES))
      Files.write(htmlPath, htmlContent(when, flakeless).getBytes)
      Files.write(jsonPath, flakeless.jsonFlightData().getBytes)
      if (!jsPath.toFile.exists()) Files.write(jsPath, report.Assets.flakelessJs.getBytes)

      System.err.println("Wrote report " + htmlPath.toAbsolutePath.toString)
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
}
