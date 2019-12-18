
name := "web-push"

organization := "org.morozov"

version := "0.2.2-sevts"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.12.8")

licenses      += ("Apache-2.0", url("http://www.apache.org/licenses/"))

libraryDependencies ++= Seq(
  "com.pauldijou" %% "jwt-core" % "4.2.0",
  "org.apache.httpcomponents" % "fluent-hc" % "4.5.2",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.55",
  "com.typesafe.akka" %% "akka-actor" % "2.6.1",
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  "com.typesafe.akka" %% "akka-stream" % "2.6.1"
)

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishMavenStyle       := true
bintrayOrganization     := None
bintrayRepository := "web-push"
bintrayPackage := "web-push"
