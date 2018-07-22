
ThisBuild / scalaVersion := "2.12.6"
ThisBuild / organization := "yangyd"

mainClass in assembly := Some("yangyd.cipher.cli")

lazy val cipherProject = (project in file("."))
  .settings(name := "cipher",
    libraryDependencies += "org.bouncycastle" % "bcprov-ext-jdk15on" % "1.60",
    libraryDependencies += "org.springframework.security" % "spring-security-core" % "5.0.6.RELEASE",
    libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0",
    libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.0",
    libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.5.0")
