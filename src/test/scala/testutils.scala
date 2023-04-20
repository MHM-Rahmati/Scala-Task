import scala.io.Source
import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.jawn.decode
import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._

import scala.util.Using
import java.nio.file.{Paths, Files}
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

object testutils {
  case class clicks(
                     impression_id: Option[String],
                     revenue: Option[Double]
                   )
  case class impressions(
                          app_id: Option[Int],
                          advertiser_id: Option[Int],
                          country_code: Option[String],
                          id: Option[String]
                        )
  // read json file and return mutable ArrayBuffer
  def jreader(fileName: String, dataType: String) = {
    val jsonString = os.read(os.pwd / "src" / "test" / "resources" / fileName)
    val data = ujson.read(jsonString)
    //data.value returns data in "ArrayBuffer" format
    if dataType=="clicks" then
      implicit val WDecoder: Decoder[clicks] = deriveDecoder
      val clicksData = for {
        doc <- parse(data.toString)
        e <- doc.as[List[clicks]]
      } yield e
      clicksData
    else
      implicit val WDecoder: Decoder[impressions] = deriveDecoder
      val impressionsData = for {
        doc <- parse(data.toString)
        e <- doc.as[List[impressions]]
      } yield e
      //      val impressionsDataList = List()
      //      impressionsData match {
      //        case Left(e) =>
      //          println(e)
      //        case Right(v) =>
      //          impressionsDataList :+ v
      //      }
      //      impressionsDataL
      impressionsData
  }

  def valHandling(x: Any) =
    x match
      case Some(x) => x
      case None => "None"

  def getCCParams(cc: AnyRef) =
    cc.getClass.getDeclaredFields.foldLeft(Map.empty[String, Any]) { (a, f) =>
      f.setAccessible(true)
      a + (f.getName -> valHandling(f.get(cc)))
    }

  def saveListAsJson(list: scala.collection.immutable.Iterable[Map[String, Matchable]], filename: String): Unit = {
    implicit val formats: Formats = DefaultFormats
    val jsonString = write(list)
    Using(Files.newBufferedWriter(Paths.get(filename))) { writer =>
      writer.write(jsonString)
    }
  }
}