package controllers

import javax.inject._

import play.api.mvc._
import play.api.http.HttpEntity
import akka.util.ByteString

import upickle.default.{ read, write, writeJs, writeBinary }
import ujson.Readable

import shared.models._
import shared.pickling._
import db.{ Database => DB }

@Singleton
class Application @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index())
  }

  def reader(readerId: Long) = Action {
    DB.readers.get(readerId) match {
      case None => BadRequest(write(ujson.Obj("status" -> "KO", "message" -> s"Not found reader of id = $readerId"))).as("application/json")
      case Some(reader) => {
        Ok(write(reader)).as("application/json")
        //        Result(
        //          header = ResponseHeader(200, Map.empty),
        //          body = HttpEntity.Strict(ByteString(writeBinary(reader)), Some("text/plain")))
        //      }
      }
    }
  }

  def saveReader = Action { request =>
    {
      try {
        request.body.asJson match {
          case Some(json) => {
            val reader = read[Reader](json.toString())
            Ok(write(ujson.Obj(
              "status" -> "OK",
              "message" -> s"Reader of id = ${reader.id} (${reader.firstName} ${reader.lastName}) accepted."))).as("application/json")
          }
          case None => throw new Exception("application/json expected")
        }
      } catch {
        case e: Exception => BadRequest(write(ujson.Obj("status" -> "KO", "message" -> e.getMessage))).as("application/json")
      }
    }
  }

  def books = Action {
    Ok(write(DB.books)).as("application/json")
  }
}
