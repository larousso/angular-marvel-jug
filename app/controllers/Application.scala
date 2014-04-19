package controllers

import play.api.mvc._
import ws.marvel.MarvelApi
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.JsValue
import play.api.libs.iteratee.{Enumerator, Enumeratee, Concurrent}
import play.api.libs.EventSource
import play.api.{Play, Logger}
import play.api.Play.current
import java.io.File


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

  def marvelFallBack() = Action {
    implicit request =>
      val file: File = Play.getFile("public/marvel.json")
      val fileContent: Enumerator[Array[Byte]] = Enumerator.fromFile(file)
      SimpleResult(
        header = ResponseHeader(200, Map(CONTENT_LENGTH -> file.length.toString)),
        body = fileContent
      ).as("application/json")
  }

}