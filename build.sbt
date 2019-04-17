
name := "web-push"

organization := "org.morozov"

version := "0.2.2"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.12.8")

libraryDependencies ++= Seq(
  "com.pauldijou" %% "jwt-core" % "0.10.0",
  "org.apache.httpcomponents" % "fluent-hc" % "4.5.2",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.55"
)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }
