package im.mange.flakeless.innards

import im.mange.little.json.{LittleJodaSerialisers, LittleSerialisers}
import org.json4s.NoTypeHints
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._
import org.json4s.native.{JsonParser, Serialization}

private [flakeless] object InvestigationJson {
  private val shoreditchFormats = Serialization.formats(NoTypeHints) ++ LittleSerialisers.all ++ LittleJodaSerialisers.all

  def serialise(r: Seq[Investigation]) = {
    implicit val formats = shoreditchFormats
    pretty(render(JsonParser.parse(write(r))))
  }
}
