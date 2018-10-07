package eagle

import java.nio.charset.{Charset, StandardCharsets}
import java.security.KeyPair
import java.util.Base64

import cats.effect.IO
import eagle.http.model.PublicKey
import javax.crypto.Cipher

package object encryption {

  def rsaKeyPair(size: Int): IO[KeyPair] = {
    import java.security.KeyPairGenerator
    IO {
      val keyGen = KeyPairGenerator.getInstance("RSA")
      keyGen.initialize(512)
      keyGen.generateKeyPair()
    }
  }

  def encrypt(publicKey: PublicKey, input: String): IO[String] = IO {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val data = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8))
    Base64.getEncoder.encodeToString(data)
  }
}
