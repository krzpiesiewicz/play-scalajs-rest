package shared

object models {

  case class Address(city: String, street: String, houseNumber: Int, flatNumber: Option[Int])

  case class Reader(id: Long, firstName: String, lastName: String, address: Address)

  sealed trait Book {
    val id: Long
    val title: String
    val author: String
  }

  object EbookFormat extends Enumeration {
    type EbookFormat = Value
    val EPUB = Value("epub")
    val MOBI = Value("mobi")
    val PDF = Value("pdf")
  }

  import EbookFormat._

  case class PaperBook(id: Long, title: String, author: String, pages: Int) extends Book

  case class Ebook(id: Long, title: String, author: String, format: EbookFormat) extends Book
}