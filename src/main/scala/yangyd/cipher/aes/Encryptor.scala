package yangyd.cipher.aes

import java.io.{InputStream, OutputStream}

import yangyd.cipher.util.Utils._

object Encryptor {
  val SALT_LENGTH = 32
  val IV_LENGTH = 16
}

class Encryptor(password: String, progress: Option[Int ⇒ Unit] = None) {

  import Encryptor._

  private val salt = randomKey(SALT_LENGTH)
  private val iv = randomKey(IV_LENGTH)
  private val cipher = AES256GCM.encryptor(password, salt, iv)

  def encrypt(outputStream: OutputStream, inputStream: InputStream): Unit = {
    outputStream.write(salt)
    outputStream.write(iv)
    transform(cipher, outputStream, inputStream, progress)
  }

}
