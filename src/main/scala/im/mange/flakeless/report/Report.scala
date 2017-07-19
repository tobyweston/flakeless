package im.mange.flakeless.report

import im.mange.flakeless.{Flakeless, report}
import org.openqa.selenium.{OutputType, TakesScreenshot}

object Report {
  import java.nio.file.{Files, Paths}

  def apply(flakeless: Flakeless) {
    //TODO: pass in root
    val filepathRoot: String = s"target/test-reports/${"%04d".format(flakeless.currentFlightNumber)}/"

    Files.createDirectories(Paths.get(filepathRoot))

    try {
      val captureImage = flakeless.rawWebDriver.asInstanceOf[TakesScreenshot].getScreenshotAs(OutputType.BYTES)

      val when = System.currentTimeMillis()

      val imagePath = Paths.get(filepathRoot + s"$when.png")
      val htmlPath = Paths.get(filepathRoot + s"$when.html")
      val jsonPath = Paths.get(filepathRoot + s"$when.json")
      val jsPath = Paths.get(filepathRoot + s"flakeless.js")

      Files.write(imagePath, captureImage)
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
