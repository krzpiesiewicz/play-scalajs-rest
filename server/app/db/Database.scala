package db

import shared.models._

object Database {
  val readers = Map[Long, Reader](
      1L -> Reader(1, "Jan", "Kowalski", Address("Warszawa", "Marszałkowska", 40, Some(16))),
      2L -> Reader(2, "Joanna", "Nowak", Address("Radzymin", "Polna", 2, None))
      )
      
  val books = Map[Long, Book](
      1L -> PaperBook(1, "Dwanaście prac Herkulesa", "Agatha Christie", 320),
      2L -> Ebook(2, "Reactive Web Applications", "Manuel Bernhardt", EbookFormat.EPUB)
      )
}