
ThisBuild / scalaVersion := "2.12.6"
ThisBuild / organization := "yangyd"

mainClass in assembly := Some("yangyd.cipher.cli")

lazy val cipherProject = (project in file("."))
  .settings(name := "cipher",
    libraryDependencies += "org.bouncycastle" % "bcprov-ext-jdk15on" % "1.60",
    libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0",
    libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.0")
