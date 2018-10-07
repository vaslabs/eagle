package eagle.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import cats.effect.IO
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import eagle.http.model.TrackingEntity
import json_support._
trait HttpRouter extends FailFastCirceSupport{ api: TrackingApi =>

  val route =  pathEndOrSingleSlash {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Eagle</h1>"))
    } ~
    put {
      complete(api.createNewUser())
    }
  } ~ path(JavaUUID) {
    userId => get {
      complete(api.publicKey(userId.toString))
    }
  }

}
