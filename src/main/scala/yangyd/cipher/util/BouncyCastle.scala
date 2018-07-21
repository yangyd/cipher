package yangyd.cipher.util

import javax.crypto.{Cipher, SecretKeyFactory}

object BouncyCastle {
  private val provider = "BCFIPS"

  def cipher(algorithm: String): Cipher = Cipher.getInstance(algorithm, provider)

  def secretKeyFactory(algorithm: String): SecretKeyFactory = SecretKeyFactory.getInstance(algorithm, provider)
}
