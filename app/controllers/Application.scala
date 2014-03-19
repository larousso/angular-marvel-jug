package controllers

import play.api.mvc._
import ws.marvel.MarvelApi
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.Json
import models.UserJsonFormats._
import models.User


object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
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

}