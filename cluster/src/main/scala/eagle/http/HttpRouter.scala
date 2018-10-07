package eagle.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{path, _}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import eagle.http.json_support._
import eagle.http.model.RecordLocation

trait HttpRouter extends FailFastCirceSupport {
  api: TrackingApi =>

  val route = pathEndOrSingleSlash {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Eagle</h1>"))
    } ~
      put {
        complete(api.createNewUser())
      } ~
      post {
        entity(as[RecordLocation]) {
          recordLocation => complete(StatusCodes.OK)
        }
      }
  } ~ path(JavaUUID) {
    userId =>
      get {
        complete(api.publicKey(userId.toString))
      }
  }
}
