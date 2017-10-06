package im.mange.flakeless.reports

import java.nio.charset.StandardCharsets
import java.util.Base64

import im.mange.flakeless.Flakeless
import im.mange.flakeless.innards.{FlightInvestigator, ReportAssets}

object TestsSummaryReport {
  import java.nio.file.{Files, Path, Paths}

  def apply(flakeless: Flakeless) {
    synchronized({
      try {
        val filepath = s"${flakeless.config.reportDirectory}/"
        Files.createDirectories(Paths.get(filepath))

        val jsonFlightData = flakeless.jsonAllFlightsData

//        println(jsonFlightData)

        val b64 = Base64.getEncoder.encodeToString(jsonFlightData.getBytes(StandardCharsets.UTF_8))

        val htmlPath = path(filepath, s"summary.html")
        write(htmlPath, htmlContent(flakeless, b64).getBytes)

        ReportAssets.writeFlakelessJs(flakeless.config.reportDirectory)

        val fileSystemReportPath = htmlPath.toAbsolutePath.toString

        flakeless.config.reportHost match {
          case None => System.err.println("*** Tests Summary Report: " + fileSystemReportPath)
          case Some(h) => System.err.println(s"*** Tests Summary Report: ${h}/${htmlPath.toString.replaceAll("\\\\", "/")} (or ${fileSystemReportPath})")

        }
        //TODO: if test failed then give link to it (or config: report all)
      } catch {
        case t: Exception => System.err.println(s"*** Failed to write Tests Summary Report something bad happened ***\nProblem was:${t.getMessage}")
      }
    })
  }

  //TODO: might be able to kill the lozenge now ...
  def htmlContent(flakeless: Flakeless, data: String) =
s"""
 |<html>
 |<head>
 |<script type="text/javascript" src="flakeless.js"></script>
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
 |img {
 |  zoom: 50%;
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
 |  <div style="font-family: Courier New;" id="content"></div>
 |  <script>
 |    var data = '${data.replaceAll("\n", "").replaceAll("'", "")}';
 |    var app = Elm.AllFlightsReport.embed(document.getElementById('content'));
 |    app.ports.allFlightsData.send(data);
 |  </script>
 |</body>
 |</html>
""".stripMargin

  private def write(path: Path, content: Array[Byte]) = Files.write(path, content)

  private def path(filepath: String, filename: String) = Paths.get(filepath + filename)
}