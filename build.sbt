
name := "web-push"

organization := "a5000"

version := "0.2.3-sevts"

scalaVersion := "2.13.1"

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

resolvers += "Nexus" at ""
credentials += Credentials("Sonatype Nexus Repository Manager", "", "admin", "xxx")
publishTo := Some("Default Role Realm" at "")
