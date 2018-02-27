import Dependencies._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.ruchij",
      scalaVersion := "2.12.4"
    )),
    name := "github-api",
    libraryDependencies ++= Seq(
      akkaActor, akkaStream, akkaHttp, akkaHttpSprayJson,
      gitHub,

      scalaTest % Test,
      scalaMock % Test,
      akkaHttpTestkit % Test,
      pegdown % Test
    )
  )

mainClass in assembly := Some("com.ruchij.web.ServerApp")
assemblyJarName in assembly := "github-api.jar"

coverageEnabled := true

testOptions in Test +=
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-results")

addCommandAlias("testWithCoverage", "; clean; test; coverageReport")