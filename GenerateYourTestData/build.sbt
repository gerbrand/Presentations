name := "generate-your-test-data-examples"

version := "0.1"

scalaVersion := "2.13.1"

lazy val akkaHttpVersion = "10.1.11"
lazy val akkaVersion    = "2.6.3"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3" % Test

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % Test

libraryDependencies += "eu.timepit" %% "refined-scalacheck" % "0.9.12" % Test

libraryDependencies += "eu.timepit" %% "refined-scalacheck" % "0.9.12" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-stream"         % akkaVersion

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
  "ch.qos.logback"    % "logback-classic"           % "1.2.3",
)

