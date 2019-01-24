package playjson

import scala.language.implicitConversions

import play.api.libs.json._
import play.api.libs.json.Json.JsValueWrapper

object jsonutils {
  
  /**
   * https://stackoverflow.com/questions/27285760/play-json-formatter-for-mapint
   */
  class MapLongWrites[T]()(implicit writes: Writes[T]) extends Writes[Map[Long, T]] {
    def writes(map: Map[Long, T]): JsValue =
      Json.obj(map.map {
        case (s, o) =>
          val ret: (String, JsValueWrapper) = s.toString -> Json.toJson(o)
          ret
      }.toSeq: _*)
  }

  /**
   * https://stackoverflow.com/questions/15488639/how-to-write-readst-and-writest-in-scala-enumeration-play-framework-2-1
   */
  object EnumUtils {
    implicit def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
      def reads(json: JsValue): JsResult[E#Value] = json match {
        case JsString(s) => {
          try {
            JsSuccess(enum.withName(s))
          } catch {
            case _: NoSuchElementException => JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
          }
        }
        case _ => JsError("String value expected")
      }
    }

    implicit def enumWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
      def writes(v: E#Value): JsValue = JsString(v.toString)
    }
  }
}