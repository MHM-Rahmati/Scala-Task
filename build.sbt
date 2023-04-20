ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "verve_task_v0.1"
  )

//Third parties
libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.9.1"
libraryDependencies += "com.lihaoyi" %% "upickle" % "3.0.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.2.0"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-core" % "4.0.3",
  "org.json4s" %% "json4s-native" % "4.0.3"
)