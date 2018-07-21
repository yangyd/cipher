
ThisBuild / scalaVersion := "2.12.6"
ThisBuild / organization := "yangyd"

//val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

lazy val cipherProject = (project in file("."))
  .settings(name := "cipher",
    libraryDependencies += "org.bouncycastle" % "bcprov-ext-jdk15on" % "1.60",
    libraryDependencies += "org.springframework.security" % "spring-security-core" % "5.0.6.RELEASE",
    libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0",

//    libraryDependencies += ""
//    libraryDependencies += "com.eed3si9n" %% "gigahorse-okhttp" % "0.3.1",
//    libraryDependencies += scalaTest % Test,
  )

