version := "0.0"

scalaVersion := "2.11.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

parallelExecution in test := false

retrieveManaged := true

