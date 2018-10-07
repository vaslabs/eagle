package eagle.http

import java.util.UUID

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.server._
import Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import eagle.http.model.{Base64, Location, RecordLocation, TrackingEntity}
import sun.security.rsa.RSAPublicKeyImpl

import scala.concurrent.Future
import scala.util.Random

class HttpRouterSpec extends WordSpec with Matchers with ScalatestRouteTest with FailFastCirceSupport{

  import json_support._

  "default http route" must {
    val uuid = UUID.randomUUID()
    val keyPair = eagle.encryption.rsaKeyPair(512).unsafeRunSync()
    val publicKeyValue = keyPair.getPublic.asInstanceOf[RSAPublicKeyImpl]
    val httpRouter = new TrackingApi with HttpRouter {

      override def createNewUser(): Future[TrackingEntity] = Future.successful(TrackingEntity(uuid, publicKeyValue))

      override def publicKey(user: String): Future[TrackingEntity] = Future.successful(TrackingEntity(uuid, publicKeyValue))

    }

    "support creating a new tracking entity" in {
      Put() ~> httpRouter.route ~> check {
        responseAs[TrackingEntity] shouldBe TrackingEntity(
          uuid, publicKeyValue
        )
      }
    }
    "give public key for each entity" in {
      Get(s"/${uuid.toString}") ~> httpRouter.route ~> check {
        responseAs[TrackingEntity] shouldBe TrackingEntity(
          uuid, publicKeyValue
        )
      }
    }
    "then posting data with that key can be decrypted" in {
      Post("/", RecordLocation(uuid, "foobar")) ~> httpRouter.route ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
  }

}
