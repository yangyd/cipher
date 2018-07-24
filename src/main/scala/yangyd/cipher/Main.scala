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

    val op = if (decrypt) "Decrypt" else "Encrypt"
    val progress: Option[Int ⇒ Unit] = if (showProgress) {
      Some { n: Int ⇒
        System.out.print(op + ": " + n + "\r")
      }
    } else {
      None
    }

    def run(): Unit = {
      for (in ← managed(input); out ← managed(output)) {
        if (decrypt) {
          new Decryptor(password, progress).decrypt(out, in)
        } else {
          new Encryptor(password, progress).encrypt(out, in)
        }
      }
    }

    try {
      run()
    } catch {
      case e: Exception ⇒
        val error = e.getClass.getSimpleName.replaceFirst("(Error$|Exception$)", "")
        fatal(op, s"${e.getMessage} ($error)")
    }

  }

  private def fatal(op: String, msg: String): Unit = {
    System.err.println(s"$op failed. $msg")
    System.exit(-1)
  }
}
