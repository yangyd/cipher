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

    arg[File]("<Input File>")
      .optional()
      .action((x, c) => c.copy(in = x))
      .text("The file to encrypt. Default from STDIN if not given.")

    opt[File]('p', "password-file")
      .optional()
      .action((x, c) => c.copy(passwordFile = x))
      .text("The file where encryption password is read. The first line of the file is used as password.\n" +
        "                           if not given, the password is read from PASSWORD environment variable.")

    opt[File]('o', "out")
      .optional()
      .valueName("<Output File>")
      .action((x, c) => c.copy(out = x))
      .text("Output file for encrypted data. Default to STDOUT if not given.")


    opt[Unit]('d', "decrypt").action((_, c) =>
      c.copy(decrypt = true)).text("Decryption mode. The cipher runs as encryption mode by default.")

    opt[Unit]('q', "quiet").action((_, c) =>
      c.copy(silence = true)).text("Do not show progress on console when not writing to STDOUT.")

    opt[Unit]("debug").hidden().action((_, c) =>
      c.copy(debug = true)).text("this option is hidden in the usage text")

    help("help").text("prints this usage text")

    note(
      """
        |
        |The data is encrypted with AES-256-GCM with randomly generated salt.
        |Generally, the same version of cipher application must be used in both encryption and decryption.
        |
        |Examples:
        |
        |Encryption:
        |  java -jar cipher.jar -p passwd_file -o encrypted input_file
        |
        |Decryption:
        |  PASSWORD=mypassword java -jar cipher.jar -d -o decrypted encrypted
        |
      """.stripMargin)
  }

  parser.parse(args, Arguments()) match {
    case Some(parsed) ⇒ invoke(parsed)
    case None ⇒ fail()
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

    if (password == null || password.trim.isEmpty) {
      fail("Password not given")
    }

    Main.invokeCipher(arg.decrypt, showProgress, password.trim, output, input)
  }

  private def fail(message: String = null): Unit = {
    if (message != null) {
      System.err.println("Error: " + message)
    }
    System.err.println()
    parser.showUsageAsError()
    System.exit(-1)
  }

  private def readPassword(fo: Option[File]): Option[String] = fo flatMap { file ⇒
    managed(new BufferedReader(new FileReader(file))).map(_.readLine()).opt
  }
}
