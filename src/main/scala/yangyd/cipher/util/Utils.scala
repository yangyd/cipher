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
        Option(cipher.update(buffer, 0, n)) foreach out.write
        if (round > PROGRESS_INTERVAL) {
          for {fn ← progress} fn(count)
          pump(count + n, 0)
        } else {
          pump(count + n, round + 1)
        }
    }

    pump(0, 0)
    out write cipher.doFinal()
  }

  def base64(data: Array[Byte]): String = B64ENCODER.encodeToString(data)
}
