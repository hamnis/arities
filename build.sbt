organization := "net.hamnaberg"

name := "arities"

version := "0.1-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

publishMavenStyle := true

val generatedPackage = "net.hamnaberg.arities"

sourceGenerators in Compile += Def.task{ Tuples.generate((sourceManaged in Compile).value, generatedPackage) }
sourceGenerators in Compile += Def.task{ Tuples.factory((sourceManaged in Compile).value, generatedPackage) }
sourceGenerators in Compile += Def.task{ Functions.generate((sourceManaged in Compile).value, generatedPackage) }

