organization := "net.hamnaberg"
name := "arities"

autoScalaLibrary := false
crossPaths := false

val generatedPackage = "net.hamnaberg.arities"

Compile / sourceGenerators += Def.task{ Tuples.generate(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage) }
Compile / sourceGenerators += Def.task{ Tuples.factory((Compile / sourceManaged).value, generatedPackage) }
Compile / sourceGenerators += Def.task{ Functions.generate(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage) }
Compile / sourceGenerators += Def.task{ Functions.generateException(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage, "IO", "java.io.IOException") }
Compile / sourceGenerators += Def.task{ Functions.generateException(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage, "SQL", "java.sql.SQLException") }
Compile / sourceGenerators += Def.task{ Functions.generateException(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage, "Checked", "java.lang.Exception") }
Compile / sourceGenerators += Def.task{ Consumers.generate(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage) }
Compile / sourceGenerators += Def.task{ Consumers.generateException(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage, "IO", "java.io.IOException") }
Compile / sourceGenerators += Def.task{ Consumers.generateException(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage, "SQL", "java.sql.SQLException") }
Compile / sourceGenerators += Def.task{ Consumers.generateException(baseDirectory.value, (Compile / sourceManaged).value, generatedPackage, "Checked", "java.lang.Exception") }


libraryDependencies += "junit" % "junit" % "4.13.1" % Test
libraryDependencies += "com.github.sbt" % "junit-interface" % "0.13.3" % Test
