package yangyd.cipher

import java.io._

import resource._
import yangyd.cipher.aes.{Decryptor, Encryptor}

object cli extends App {
  private val PASSWORD_ENV = "PASSWORD"

  case class Arguments(passwordFile: File = null,
                       decrypt: Boolean = false,
                       out: File = null,
                       in: File = null,
                       silence: Boolean = false,
                       debug: Boolean = false)

  val parser = new scopt.OptionParser[Arguments]("cipher") {
    head("cipher", "1.0")

    opt[File]('p', "password-file")
      .optional()
      .action((x, c) => c.copy(passwordFile = x))
      .text("The file from which password is read. The first line is used as password." +
        " if not given, the password is read from password environment variable")

    opt[File]('o', "out")
      .optional()
      .valueName("<Output File>")
      .action((x, c) => c.copy(out = x))
      .text("Output file for encrypted data. Data is written to STDOUT if not given.")

    arg[File]("<Input File>")
      .optional()
      .action((x, c) => c.copy(in = x))
      .text("The file to encrypt. Data is read from STDIN if not given.")

    opt[Unit]('d', "decrypt mode").action((_, c) =>
      c.copy(decrypt = true)).text("Decryption mode. Default is Encryption mode")

    opt[Unit]('q', "quiet").action((_, c) =>
      c.copy(silence = true)).text("Do not show progress")

    opt[Unit]("debug").hidden().action((_, c) =>
      c.copy(debug = true)).text("this option is hidden in the usage text")

    help("help").text("prints this usage text")

    note("some notes.")
  }

  parser.parse(args, Arguments()) match {
    case Some(parsed) ⇒ invoke(parsed)
    case None ⇒ parser.showTryHelp()
  }

  private def invoke(arg: Arguments): Unit = {
    val password = readPassword(Option(arg.passwordFile)) match {
      case Some(pw) ⇒
        pw
      case None ⇒
        System.getenv(PASSWORD_ENV)
    }

    var showProgress = !arg.silence

    val input = Option(arg.in) match {
      case Some(file) ⇒ new FileInputStream(file)
      case None ⇒ System.in
    }

    val output = Option(arg.out) match {
      case Some(file) ⇒ new FileOutputStream(file)
      case None ⇒
        showProgress = false
        System.out
    }

    Main.invokeCipher(arg.decrypt, showProgress, password, output, input)
  }

  private def readPassword(fo: Option[File]): Option[String] = fo flatMap { file ⇒
    managed(new BufferedReader(new FileReader(file))).map(_.readLine()).opt
  }
}
