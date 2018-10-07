package eagle

import java.security.KeyPair

import cats.effect.IO

package object encryption {

  def rsaKeyPair(size: Int): IO[KeyPair] = {
    import java.security.KeyPairGenerator
    IO {
      val keyGen = KeyPairGenerator.getInstance("RSA")
      keyGen.initialize(512)
      keyGen.generateKeyPair()
    }
  }
}
