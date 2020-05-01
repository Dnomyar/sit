import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "Git in Scala",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "dev.zio" %% "zio" % "1.0.0-RC18-2",
    libraryDependencies += "dev.zio" %% "zio-streams" % "1.0.0-RC18-2",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0",
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.1" % "test",
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
