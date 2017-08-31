package im.mange.flakeless.reporting

import im.mange.flakeless.{Flakeless, FlightInvestigator}

object AllFlightsSummaryReport {

  def apply(flakeless: Flakeless, outputDirectory: String, captureImage: Boolean = true, host: Option[String] = None) {
    try {
      FlightInvestigator.summarise()
    } catch {
      case t: Exception => System.err.println(s"*** Failed to write report something bad happened ***\nProblem was:${t.getMessage}")
    }
  }
}
