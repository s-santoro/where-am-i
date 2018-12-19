name := """whereami"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.7"

libraryDependencies += guice

// PostgreSQL Database
// superuser: postgres
// password: database
libraryDependencies ++= Seq(
  evolutions,
  javaJdbc,
  javaJpa,
  "org.postgresql" % "postgresql" % "42.2.2",
  "org.hibernate" % "hibernate-entitymanager" % "5.3.7.Final"
)

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "4.0.0",
  "org.webjars" % "jquery" % "3.2.1",
  "org.webjars" % "popper.js" % "1.12.9",
  "org.webjars.npm" % "tooltip.js" % "1.2.0",
  "org.webjars" % "sammy" % "0.7.4",
  "org.webjars.npm" % "leaflet" % "1.3.4",
  "org.webjars" % "chartjs" % "26962ce-1"
)

PlayKeys.externalizeResources := false

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test


// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// heroku route
herokuAppName in Compile := "where-am-i-located"