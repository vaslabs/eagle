package eagle.http

import eagle.http.model.TrackingEntity

import scala.concurrent.Future

trait TrackingApi {

  def createNewUser(): Future[TrackingEntity]
  def publicKey(user: String): Future[TrackingEntity]
}
