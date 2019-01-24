package playjson

import play.api.libs.json._
import play.api.libs.json.Json.JsValueWrapper

import playjson.jsonutils._

import shared.models._

object BooksAndReadersFormats {

  implicit val addressFormat = Json.format[Address]
  implicit val readerFormat = Json.format[Reader]
  implicit val ebookFormatEnumReads: Reads[EbookFormat.Value] = EnumUtils.enumReads(EbookFormat)

  val ebookFormat = Json.format[Ebook]
  val paperBookFormat = Json.format[PaperBook]

  implicit val ebookReads = Json.reads[Ebook]
  implicit val paperBookReads = Json.reads[PaperBook]

  implicit object bookWrites extends Writes[Book] {
    def writes(book: Book) = book match {
      case pb: PaperBook => paperBookFormat.writes(pb)
      case eb: Ebook => ebookFormat.writes(eb)
    }
  }

  implicit val bookMapWrites = new MapLongWrites[Book]
}