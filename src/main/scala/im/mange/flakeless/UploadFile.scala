package im.mange.flakeless

import im.mange.flakeless.innards.{Body, Command, Description, WaitForInteractableElement}
import org.openqa.selenium.{By, OutputType, TakesScreenshot, WebElement}

//TODO: feels like I should be a factory for a private class ... see: Click
object UploadFile {
  def apply(flakeless: Flakeless, by: By, filename: String): Unit = {
    apply(Body(flakeless.rawWebDriver), by, filename, Some(flakeless))
  }

  def apply(in: WebElement, by: By, filename: String, flakeless: Option[Flakeless] = None): Unit = {
    WaitForInteractableElement(flakeless,
      Command("UploadFile", Some(in), Some(by), args = Map("filename" -> filename)),
      description = e => Description().describeActual(e),
      action = e => e.sendKeys(filename),
      mustBeDisplayed = false
    )
  }
}


object Report {
  import java.nio.file._
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
      if (!jsPath.toFile.exists()) Files.write(jsPath, report.Report.flakelessJs.getBytes)

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