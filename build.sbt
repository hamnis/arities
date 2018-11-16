organization := "net.hamnaberg"
name := "arities"

autoScalaLibrary := false
crossPaths := false

val generatedPackage = "net.hamnaberg.arities"

sourceGenerators in Compile += Def.task{ Tuples.generate(baseDirectory.value, (sourceManaged in Compile).value, generatedPackage) }
sourceGenerators in Compile += Def.task{ Tuples.factory((sourceManaged in Compile).value, generatedPackage) }
sourceGenerators in Compile += Def.task{ Functions.generate(baseDirectory.value, (sourceManaged in Compile).value, generatedPackage) }
sourceGenerators in Compile += Def.task{ Functions.generateException(baseDirectory.value, (sourceManaged in Compile).value, generatedPackage, "IO", "java.io.IOException") }
sourceGenerators in Compile += Def.task{ Functions.generateException(baseDirectory.value, (sourceManaged in Compile).value, generatedPackage, "SQL", "java.sql.SQLException") }
sourceGenerators in Compile += Def.task{ Consumers.generate(baseDirectory.value, (sourceManaged in Compile).value, generatedPackage) }
sourceGenerators in Compile += Def.task{ Consumers.generateException(baseDirectory.value, (sourceManaged in Compile).value, generatedPackage, "IO", "java.io.IOException") }
sourceGenerators in Compile += Def.task{ Consumers.generateException(baseDirectory.value, (sourceManaged in Compile).value, generatedPackage, "SQL", "java.sql.SQLException") }


libraryDependencies += "junit" % "junit" % "4.12" % Test
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
