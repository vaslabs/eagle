package eagle.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

trait HttpRouter{

  val route =  path("") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Eagle</h1>"))
    }
  }

}
