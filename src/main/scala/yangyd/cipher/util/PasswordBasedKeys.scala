package yangyd.cipher.util

import javax.crypto.spec.{PBEKeySpec, SecretKeySpec}

object PasswordBasedKeys {

  private val KEY_ALGORITHM = "PBKDF2WithHmacSHA256"
  private val ITERATION_COUNT = 1024
  private val KEY_LENGTH = 256

  private def encode(password: String, salt: Array[Byte]): Array[Byte] =
    secretKey(new PBEKeySpec(password.toCharArray, salt, ITERATION_COUNT, KEY_LENGTH)).getEncoded

  private def secretKey(keySpec: PBEKeySpec) =
    BouncyCastle.secretKeyFactory(KEY_ALGORITHM).generateSecret(keySpec)

  def keySpec(password: String, salt: Array[Byte], algorithm: String) =
    new SecretKeySpec(encode(password, salt), algorithm)

}
