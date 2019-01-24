import scala.scalajs.js
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.typedarray.TypedArrayBuffer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.Node
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.XMLHttpRequest

import upickle.default.{ read, readBinary }
import upickle.WebJson
import upack.Readable

import shared.models._
import shared.pickling._

object BooksAndReaders {

  def main(args: Array[String]): Unit = {

    refreshAll()
    dom.window.setInterval(refreshAll, 3000)

    Ajax.get("/reader/1").map {
      case req: XMLHttpRequest => {
        val reader = read[Reader](req.responseText)
        println(reader)
      }
    }
  }

  val refreshAll = () => {
    println("Tu odświeżanie")
    replaceBooksHtml(getBooks)
  }

  def getBooks = Ajax.get("/books").map {
    case req: XMLHttpRequest => {
      val books = read[Map[Long, Book]](req.responseText)
      println(books)
      books
    }
  }

  def replaceBooksHtml(fut: Future[Map[Long, Book]]) = {
    fut map {
      case books => {
        val oldBookList = dom.window.document.getElementById("books")
        val newBookList = books.values.foldLeft(dom.document.createElement("ul")) { (node, book) =>
          {
            val li = dom.document.createElement("li")
            li.innerHTML = getBookHtml(book)
            node.appendChild(li)
            node
          }
        }
        oldBookList.parentNode.replaceChild(newBookList, oldBookList)
      }
    }
  }

  def getBookHtml(book: Book) = {
    s"""
    |<table>
    |<tr><th>tytuł:</th><td>${book.title}</td></tr>
    |<tr><th>autor:</th><td>${book.author}</td></tr>""".stripMargin +
      (book match {
        case pb: PaperBook => s"""
        |<tr><th>liczba stron:</th><td>${pb.pages}</td></tr>
        """.stripMargin
        case eb: Ebook => s"""
        |<tr><th>format:</th><td>${eb.format}</td></tr>
        """.stripMargin
      }) +
      s"""
    |</table>""".stripMargin
  }

//  def getReaderBinary() =
//    Ajax.get("/reader/1", responseType = "arraybuffer")
//      .map {
//        case req: XMLHttpRequest => {
//          println(req.response)
//          val arrayBuffer = req.response.asInstanceOf[ArrayBuffer]
//          val typedBuffer = TypedArrayBuffer.wrap(arrayBuffer)
//          println(typedBuffer)
//          val out = typedBuffer.slice()
//          //          println(out.array)
//          //          val array = js.Array(out.)
//          //          val array = typedBuffer.array()
//          //          println(array)
//
//          //          val reader = readBinaryF[Reader](Readable.out)
//          //          println(reader)
//        }
//      }
}
