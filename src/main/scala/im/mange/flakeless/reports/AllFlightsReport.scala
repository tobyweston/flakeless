package im.mange.flakeless.reports

import java.nio.charset.StandardCharsets
import java.util.Base64

import im.mange.flakeless.Flakeless
import im.mange.flakeless.innards.{FlightInvestigator, ReportAssets}

object AllFlightsReport {
  import java.nio.file.{Files, Path, Paths}

  def apply(flakeless: Flakeless, outputDirectory: String, host: Option[String] = None) {
    synchronized({
      try {
        val filepath = s"$outputDirectory/"
        Files.createDirectories(Paths.get(filepath))

        val jsonFlightData = flakeless.jsonAllFlightsData
        val b64 = Base64.getEncoder.encodeToString(jsonFlightData.getBytes(StandardCharsets.UTF_8))

        val htmlPath = path(filepath, s"report.html")
        write(htmlPath, htmlContent(flakeless, b64).getBytes)

        ReportAssets.writeFlakelessJs(outputDirectory)

        val fileSystemReportPath = htmlPath.toAbsolutePath.toString

        host match {
          case None => System.err.println("*** Flakeless All Flights report: " + fileSystemReportPath)
          case Some(h) => System.err.println(s"*** Flakeless All Flights report: ${h}/${htmlPath.toString.replaceAll("\\\\", "/")} (or ${fileSystemReportPath})")

        }
        //TODO: write file ... if it already exists then delete it
        //TODO: follow the pattern from FlightReport
        //TODO: if test failed then give link to it (or config: report all)
        FlightInvestigator.summarise()
      } catch {
        case t: Exception => System.err.println(s"*** Failed to write All Flights report something bad happened ***\nProblem was:${t.getMessage}")
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
 |    app.ports.data.send(data);
 |  </script>
 |</body>
 |</html>
""".stripMargin

  private def write(path: Path, content: Array[Byte]) = Files.write(path, content)

  private def path(filepath: String, filename: String) = Paths.get(filepath + filename)
}
