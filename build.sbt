name := "rescue-time-export"

version := "0.1"

scalaVersion := "2.10.2"

seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.2.2"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3.5"

libraryDependencies += "io.spray" % "spray-http" % "1.3.1"

libraryDependencies += "io.spray" % "spray-json_2.10" % "1.2.6"

libraryDependencies += "io.spray" % "spray-client" % "1.3.1"

libraryDependencies += "io.spray" % "spray-can" % "1.3.1"

libraryDependencies += "com.google.guava" % "guava" % "17.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.7"
