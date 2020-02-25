name := "generate-your-test-data-examples"

version := "0.1"

scalaVersion := "2.13.1"

lazy val akkaHttpVersion = "10.1.11"
lazy val akkaVersion = "2.6.3"
val circeVersion = "0.13.0"
val refinedVersion = "0.9.12"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % Test
libraryDependencies += "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion

val jsonDependencies: List[ModuleID] = List(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-literal" % circeVersion,
  "io.circe" %% "circe-refined" % circeVersion
)

libraryDependencies ++= Seq(
  "eu.timepit" %% "refined" % refinedVersion,
  "eu.timepit" %% "refined-scalacheck" % refinedVersion % Test
)


libraryDependencies ++= Seq(
  "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "org.threeten" % "threeten-extra" % "1.5.0",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
)

libraryDependencies ++= jsonDependencies





