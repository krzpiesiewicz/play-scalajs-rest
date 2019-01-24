package shared

import scala.language.implicitConversions

import upickle.default.{ ReadWriter => RW, readwriter => rw, read, write, writeJs, macroRW }

import models._

object pickling {
 
    implicit val rwAddress: RW[Address] = macroRW
    implicit val rwReader: RW[Reader] = macroRW
    implicit val rwEbookFormat = enumRW(EbookFormat)

    implicit val rwPaperBook: RW[PaperBook] = macroRW
    implicit val rwEbook: RW[Ebook] = macroRW
    implicit val rwBook: RW[Book] = macroRW
    
    implicit def enumRW[E <: Enumeration](e: E): RW[E#Value] =
      rw[ujson.Value].bimap[E#Value](
        enum => writeJs(enum.toString),
        json => {
          val s = writeJs(json).str
          try {
            e.withName(s)
          } catch {
            case _: NoSuchElementException => throw new Exception(s"Enumeration expected of type: '${e.getClass}', but it does not appear to contain the value: '$s'")
          }
        })
}