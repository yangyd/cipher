package yangyd.cipher.aes

import java.security.spec.AlgorithmParameterSpec

import javax.crypto.spec.GCMParameterSpec
import javax.crypto.{Cipher, SecretKey}
import yangyd.cipher.util.{BouncyCastle, PasswordBasedKeys}

object AES256GCM {
  private val AES = "AES"
  private val ALGORITHM = "AES/GCM/NoPadding"
  private val GCM_TLEN = 128

  def decryptor(password: String, salt: Array[Byte], iv: Array[Byte]): Cipher =
    cipher(Cipher.DECRYPT_MODE, password, salt, iv)

  def encryptor(password: String, salt: Array[Byte], iv: Array[Byte]): Cipher =
    cipher(Cipher.ENCRYPT_MODE, password, salt, iv)

  private def cipher(mode: Int, password: String, salt: Array[Byte], iv: Array[Byte]): Cipher =
    cipher(mode, PasswordBasedKeys.keySpec(password, salt, AES), new GCMParameterSpec(GCM_TLEN, iv))

  private def cipher(mode: Int, secretKey: SecretKey, algorithmParameterSpec: AlgorithmParameterSpec): Cipher = {
    val cipher = BouncyCastle.cipher(ALGORITHM)
    cipher.init(mode, secretKey, algorithmParameterSpec)
    cipher
  }

}
