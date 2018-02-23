import sbt._

object Dependencies
{
  val AKKA_VERSION = "2.5.9"
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % AKKA_VERSION
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % AKKA_VERSION

  val AKKA_HTTP_VERSION = "10.0.11"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % AKKA_HTTP_VERSION
  lazy val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % AKKA_HTTP_VERSION

  lazy val gitHub = "org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.5"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}