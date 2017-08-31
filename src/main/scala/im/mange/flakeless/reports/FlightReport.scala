package im.mange.flakeless.reports

import java.nio.charset.StandardCharsets
import java.util.Base64

import im.mange.flakeless.Flakeless
import im.mange.flakeless.innards.ReportAssets
import org.openqa.selenium.{OutputType, TakesScreenshot}

object FlightReport {
  import java.nio.file.{Files, Path, Paths}

  def apply(flakeless: Flakeless, outputDirectory: String, captureImage: Boolean = true, host: Option[String] = None) {
    try {
      val flightNumber = flakeless.getCurrentFlightNumber
      val filepath = s"$outputDirectory/${"%04d".format(flightNumber)}/"
      Files.createDirectories(Paths.get(filepath))

      val when = System.currentTimeMillis()

      val imagePath = path(filepath, s"$when.png")
      if (captureImage) write(imagePath, screenshot(flakeless))

      val jsonFlightData = flakeless.jsonFlightData(flightNumber)
      val b64 = Base64.getEncoder.encodeToString(jsonFlightData.getBytes(StandardCharsets.UTF_8))

      val htmlPath = path(filepath, s"report.html")
      write(htmlPath, htmlContent(when, flakeless, b64).getBytes)

      ReportAssets.writeFlakelessJs(outputDirectory)

      val fileSystemReportPath = htmlPath.toAbsolutePath.toString

      host match {
        case None => System.err.println("*** Flakeless Flight report: " + fileSystemReportPath)
        case Some(h) => System.err.println(s"*** Flakeless Flight report: ${h}/${htmlPath.toString.replaceAll("\\\\", "/")} (or ${fileSystemReportPath})")

      }
    } catch {
      case t: Exception => System.err.println(s"*** Failed to write Flight report something bad happened ***\nProblem was:${t.getMessage}")
    }

    //TODO: might be able to kill the lozenge now ...
    def htmlContent(when: Long, flakeless: Flakeless, data: String) =
s"""
  |<html>
  |<head>
  |<script type="text/javascript" src="../flakeless.js"></script>
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
  |.container {
  |  width: 100%;
  |  height: auto;
  |}
  |.container img {
  |  width: 100%;
  |  height: auto;
  |}
  |
  |ul {
  |  list-style: none;
  |  padding: 0;
  |  margin: 0;
  |}
  |
  |li {
  |  padding-left: 1em;
  |  text-indent: -.7em;
  |}
  |
  |.message::before {
  |  content: "• ";
  |  font-size: x-large;
  |  color: grey;
  |}
  |.pass::before {
  |  content: "• ";
  |  font-size: x-large;
  |  color: #00cc00;
  |}
  |.fail::before {
  |  content: "• ";
  |  font-size: x-large;
  |  color: cc0000;
  |}
  |.dunno::before {
  |  content: "• ";
  |  font-size: x-large;
  |  color: cccc00;
  |}
  |</style>
  |</head>
  |<body>
  |  <table>
  |  <tr>
  |  <td style="width 50%;"><div style="font-family: Courier New;" id="content"></div></td>
  |  <td style="width 50%;"><div style="background-color: grey;" class="container"><img src="$when.png"></div></td>
  |  </tr>
  |  </table>
  |  <script>
  |    var data = '${data.replaceAll("\n", "").replaceAll("'", "")}';
  |    var app = Elm.FlightReport.embed(document.getElementById('content'));
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
