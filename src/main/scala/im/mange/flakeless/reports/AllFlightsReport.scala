package im.mange.flakeless.reports

import im.mange.flakeless.Flakeless
import im.mange.flakeless.innards.FlightInvestigator

object AllFlightsReport {

  def apply(flakeless: Flakeless, outputDirectory: String, host: Option[String] = None) {
    try {
      //TODO: write file ... if it already exists then delete it
      //TODO: follow the pattern from FlightReport
      //TODO: if test failed then give link to it (or config: report all)
      FlightInvestigator.summarise()
    } catch {
      case t: Exception => System.err.println(s"*** Failed to write report something bad happened ***\nProblem was:${t.getMessage}")
    }
  }
}
