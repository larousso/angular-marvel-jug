package models

import play.modules.reactivemongo.ReactiveMongoPlugin._
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.Some
import scala.concurrent.Future
import play.api.libs.json.Json
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import models.UserJsonFormats._


/**
 * Classe user
 * Created by adelegue on 25/02/2014.
 */
case class User(id: String, email: String, password: String, coord: Option[Coord], preferedCharacter: Option[String], favorites: List[Favorite] = List()) {

  def save() : Future[User] = {
    User.save(this)
  }

  def addFavorite(mayBeFavorite: Option[Favorite]): User = {
    mayBeFavorite match {
      case Some(favorite) => User(id, email, password, coord, preferedCharacter, favorite :: favorites)
      case None => this
    }
  }

  def update(): Future[User] = {
    User.update(this)
  }
}

case class Coord(lat: BigDecimal, lng: BigDecimal)

case class Favorite(idMarvel: String)

case class Favorites(favorites: List[Favorite])

object UserJsonFormats {

  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._

  implicit val coordFormat = Json.format[Coord]
  implicit val favoritesFormat = Json.format[Favorite]
  implicit val userFormat = Json.format[User]

  implicit val coordWrites = Json.writes[Coord]
  implicit val favoriteWrites = Json.writes[Favorite]
  implicit val userWrites = Json.writes[User]


  val coordMapping = mapping(
    "lat" -> bigDecimal,
    "lng" -> bigDecimal
  )(Coord.apply)(Coord.unapply)
  val coordForm = Form(coordMapping)

  val favoriteMapping = mapping("idMarvel" -> nonEmptyText)(Favorite.apply)(Favorite.unapply)

  val favoriteForm = Form{
    optional(favoriteMapping)
  }

  val userForm = Form {
    mapping(
      "id" -> nonEmptyText,
      "email" -> email,
      "password" -> nonEmptyText,
      "coord" -> optional(coordMapping),
      "preferedCharacter" -> optional(text),
      "favorites" -> list(favoriteMapping)
    )(User.apply)(User.unapply)
  }
}

object User {
  def users: JSONCollection = db.collection[JSONCollection]("users")


  def save(user: User): Future[User] = {
    users.save(user).map(le => user)
  }

  def update(user: User): Future[User] = {
    users.update(Json.obj("id" -> user.id), user).map(le => user)
  }

  def find(id: String): Future[Option[User]] = {
    users.find(Json.obj("id" -> id)).one[User]
  }
}