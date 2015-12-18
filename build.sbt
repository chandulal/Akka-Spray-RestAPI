name := """hello-akka"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= {
  val sprayV = "1.3.3"
  val akkaV = "2.3.11"
  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-testkit" % sprayV % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.specs2" %% "specs2-core" % "2.3.11" % "test",
    "org.json4s" % "json4s-native_2.11" % "3.3.0"
  )
}
