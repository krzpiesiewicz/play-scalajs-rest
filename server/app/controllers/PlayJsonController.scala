package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.json._

import shared.models._
import db.{ Database => DB }

import playjson.BooksAndReadersFormats._

@Singleton
class PlayJsonController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def reader(readerId: Long) = Action {
    DB.readers.get(readerId) match {
      case None => BadRequest(Json.obj("status" -> "KO", "message" -> s"Not found reader of id = $readerId"))
      case Some(reader) => {
        Ok(Json.toJson(reader))
      }
    }
  }
  
  def saveReader = Action(parse.json) { request =>
    val result = request.body.validate[Reader]
    result.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      reader => {
        Ok(Json.obj("status" -> "OK", "message" -> (s"Reader of id = ${reader.id} (${reader.firstName} ${reader.lastName}) accepted.")))
      })
  }

  implicit def books = Action {
    Ok(Json.toJson(DB.books))
  }
}
