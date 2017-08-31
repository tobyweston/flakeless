package im.mange.flakeless.innards

private [flakeless] object ReportingAssets {
  val flakelessJs = io.Source.fromResource("flakeless.js").mkString
}
