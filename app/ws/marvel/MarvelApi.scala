package ws.marvel

import play.api.Play
import java.util.Date
import play.api.libs.ws.WS
import play.api.libs.ws.WS.WSRequestHolder
import play.api.Play.current
import play.api.libs.Codecs

/**
 * Created by adelegue on 24/02/2014.
 */
object MarvelApi {

  val MD5 = "MD5"
  val publicKey = Play.configuration.getString("marvel.public.key").getOrElse("")
  val privateKey = Play.configuration.getString("marvel.private.key").getOrElse("")
  val marvel = "http://gateway.marvel.com/v1/public/"

  def wrap(path: String) : WSRequestHolder = {
    val time = new Date().getTime.toString;
    val toHash = time+privateKey+publicKey
    val hash = Codecs.md5(toHash.getBytes())
    WS.url(marvel+path).withQueryString("ts"->time, "hash" -> hash, "apikey" -> publicKey)
  }

}
