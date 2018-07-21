package yangyd.cipher.util

import java.io.{InputStream, OutputStream}
import java.security.SecureRandom
import java.util.Base64

import javax.crypto.Cipher

import scala.annotation.tailrec

object Utils {
  private val BUF_SIZE = 16 * 1024
  private val EOF = -1
  private val PROGRESS_INTERVAL = 20

  private val SECURE_RANDOM = new SecureRandom()
  private val B64ENCODER = Base64.getEncoder

  def randomKey(length: Int): Array[Byte] = {
    val bytes = new Array[Byte](length)
    SECURE_RANDOM.nextBytes(bytes)
    bytes
  }

  def transform(cipher: Cipher, out: OutputStream, in: InputStream, progress: Option[Int ⇒ Unit]): Unit = {
    val buffer = new Array[Byte](BUF_SIZE)
    @tailrec def pump(count: Int, round: Int): Int = in.read(buffer) match {
      case EOF ⇒ count
      case n ⇒
        out write cipher.update(trim(buffer, n))
        if (round > PROGRESS_INTERVAL) {
          for { fn ← progress } fn(count)
          pump(count + n, 0)
        } else {
          pump(count + n, round + 1)
        }
    }
  }

  def base64(data: Array[Byte]): String = B64ENCODER.encodeToString(data)

  private def trim(data: Array[Byte], n: Int): Array[Byte] =
    if (n == data.length) data else java.util.Arrays.copyOf(data, n)
}
