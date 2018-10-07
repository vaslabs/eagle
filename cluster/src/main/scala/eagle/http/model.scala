package eagle.http

import java.util.UUID

import eagle.http.model.TrackingEntity
import io.circe.{Decoder, Encoder, Json}
import sun.security.rsa.RSAPublicKeyImpl

object model {

  type PublicKey = RSAPublicKeyImpl

  case class TrackingEntity(uuid: UUID, publicKey: PublicKey)

}

object json_support {

  import io.circe.generic.auto._
  import io.circe.generic.semiauto._
  import cats.syntax.either._

  implicit val publicKeyEncoder: Encoder[model.PublicKey] = Encoder.instance[model.PublicKey] {
    publicKey =>
      val exponent = Json.fromBigInt(publicKey.getPublicExponent)
      val modulus = Json.fromBigInt(publicKey.getModulus)
      Json.obj("exponent" -> exponent, "modulus" -> modulus)
  }

  implicit val publicKeyDecoder: Decoder[model.PublicKey] = Decoder.instance[model.PublicKey] {
    cursor => for {
      exponent <- cursor.downField("exponent").as[BigInt]
      modulus <- cursor.downField("modulus").as[BigInt]
    } yield new RSAPublicKeyImpl(modulus.bigInteger, exponent.bigInteger)
  }

  implicit val trackingEntityEncoder: Encoder[TrackingEntity] = deriveEncoder[TrackingEntity]
  implicit val trackingEntityDecoder: Decoder[TrackingEntity] = deriveDecoder[TrackingEntity]

}
