package yangyd.cipher

import java.io.{InputStream, OutputStream}

import resource.managed
import yangyd.cipher.aes.{Decryptor, Encryptor}

object Main {
  def invokeCipher(decrypt: Boolean,
                   showProgress: Boolean,
                   password: String,
                   output: OutputStream,
                   input: InputStream): Unit = {

    val progress: Option[Int ⇒ Unit] = if (showProgress) {
      None
    } else {
      Some({n: Int ⇒ println(s"Encrypted: $n\r")})
    }

    for (in ← managed(input); out ← managed(output)) {
      if (decrypt) {
        new Decryptor(password, progress).decrypt(out, in)
      } else {
        new Encryptor(password, progress).encrypt(out, in)
      }
    }
  }

}
