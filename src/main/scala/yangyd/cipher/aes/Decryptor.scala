package yangyd.cipher.aes

import java.io.{InputStream, OutputStream}

import yangyd.cipher.util.Utils

object Decryptor {

  private def readHead(in: InputStream, n: Int) = {
    val chunk = new Array[Byte](n)
    val read = in.read(chunk)
    if (read != n) {
      throw new IllegalStateException("invalid input size")
    }
    chunk
  }
}

class Decryptor(password: String, progress: Option[Int ⇒ Unit] = None) {

  import Decryptor._

  def decrypt(out: OutputStream, in: InputStream): Unit = {
    val salt = readHead(in, Encryptor.SALT_LENGTH)
    val iv = readHead(in, Encryptor.IV_LENGTH)
    val cipher = AES256GCM.decryptor(password, salt, iv)
    Utils.transform(cipher, out, in, progress)
  }

}
