package im.mange.flakeless.innards

import java.nio.file.{Files, Path, Paths}

private [flakeless] object ReportAssets {
  private val jsFilename = "flakeless.js"
  private val flakelessJs = scala.io.Source.fromResource(jsFilename).mkString

  def writeFlakelessJs(outputDirectory: String) = {
    val jsPath = path(outputDirectory + "/", s"$jsFilename")
    if (!jsPath.toFile.exists()) write(jsPath, ReportAssets.flakelessJs.getBytes)
  }

  //TODO: should all use little instead
  private def path(filepath: String, filename: String) = Paths.get(filepath + filename)
  private def write(path: Path, content: Array[Byte]) = Files.write(path, content)
}
