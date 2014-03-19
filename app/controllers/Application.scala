package controllers

import play.api.mvc._
import ws.marvel.MarvelApi
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{JsValue, Json}
import models.UserJsonFormats._
import models.User
import play.api.libs.iteratee.{Enumeratee, Concurrent}
import play.api.libs.EventSource
import play.api.Logger


object Application extends Controller {

  val (chatOut, chatChannel) = Concurrent.broadcast[JsValue]

  def index = Action {
    Ok(views.html.index())
  }

  def chatFeed = Action { req =>
    Logger.info(s"${req.remoteAddress} - SSE connected")
    Ok.feed(chatOut
      &> Concurrent.buffer(50)
      &> connDeathWatch(req.remoteAddress)
      &> EventSource()
    ).as("text/event-stream")
  }

  def connDeathWatch(addr: String): Enumeratee[JsValue, JsValue] =
    Enumeratee.onIterateeDone{ () => Logger.info(s"$addr - SSE disconnected") }

  def postMessage = Action(parse.json) { req =>
    Logger.info(s"new message ${req.body}")
    chatChannel.push(req.body)
    Ok
  }

  def marvel(path: String) = Action.async {
    implicit request =>
      val queryString: Map[String, Seq[String]] = request.queryString
      if (queryString.isEmpty) {
        MarvelApi.wrap(path).get().map(r => Ok(r.body))
      } else {
        val query = queryString.flatMap(entry => entry._2.map(v => (entry._1, v))).toList
        MarvelApi.wrap(path)
          .withQueryString(query: _*)
          .get()
          .map(r => Ok(r.body))
      }
  }

  def createUser = Action.async {
    implicit request =>
      val form = userForm.bindFromRequest()
      if (form.hasErrors) {
        scala.concurrent.Future {
          BadRequest(form.errorsAsJson)
        }
      } else {
        val u = form.get
        u.save().map(lastError => Ok(Json.toJson(u)))
      }
  }

  def setBestSuperHero() {

  }

  def addFavorite(idUser: String) = Action.async {
    implicit request =>
      favoriteForm.bindFromRequest.fold(
        formWithErrors => {
          scala.concurrent.Future {
            BadRequest(formWithErrors.errorsAsJson)
          }
        }, fav => {
          User.find(idUser).flatMap {
            case Some(u) => u.addFavorite(fav).update().map(u => Ok(Json.toJson(u)))
            case None => scala.concurrent.Future {
              Ok
            }
          }
        }
      )
  }
}