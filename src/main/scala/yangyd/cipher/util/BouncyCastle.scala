package yangyd.cipher.util

import javax.crypto.{Cipher, SecretKeyFactory}
import org.bouncycastle.jce.provider.BouncyCastleProvider

object BouncyCastle {
  private val provider = BouncyCastleProvider.PROVIDER_NAME
  java.security.Security.addProvider(new BouncyCastleProvider)

  def cipher(algorithm: String): Cipher = Cipher.getInstance(algorithm, provider)

  def secretKeyFactory(algorithm: String): SecretKeyFactory = SecretKeyFactory.getInstance(algorithm, provider)
}
