scalaVersion := "2.12.6"

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "org.slf4j" % "slf4j-nop" % "1.7.25",
  "com.h2database" % "h2" % "1.4.197",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

scalacOptions += "-deprecation"

fork in run := true
