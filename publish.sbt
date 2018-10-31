disablePlugins(aether.AetherPlugin)
enablePlugins(aether.SignedAetherPlugin)

overridePublishSignedSettings
overridePublishLocalSettings

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }

publishTo := {
  if (isSnapshot.value) {
    Some(Opts.resolver.sonatypeSnapshots)
  } else {
    Some(Opts.resolver.sonatypeStaging)
  }
}
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

packageOptions += {
  val title  = name.value
  val ver    = version.value
  val vendor = organization.value

  Package.ManifestAttributes(
    "Created-By"               -> "Scala Build Tool",
    "Built-By"                 -> System.getProperty("user.name"),
    "Build-Jdk"                -> System.getProperty("java.version"),
    "Specification-Title"      -> title,
    "Specification-Version"    -> ver,
    "Specification-Vendor"     -> vendor,
    "Implementation-Title"     -> title,
    "Implementation-Version"   -> ver,
    "Implementation-Vendor-Id" -> vendor,
    "Implementation-Vendor"    -> vendor,
    "Automatic-Module-Name"    -> "net.hamnaberg.arities"
  )
}
releasePublishArtifactsAction := PgpKeys.publishSigned.value

homepage := Some(url("https://github.com/hamnis/arities"))
startYear := Some(2018)
licenses := Seq(
  "Apache2" -> url("https://github.com/hamnis/arities/blob/master/LICENSE.txt")
)
scmInfo := Some(
  ScmInfo(
    new URL("https://github.com/hamnis/arities"),
    "scm:git:git@github.com:hamnis/arities.git",
    Some("scm:git:git@github.com:hamnis/arities.git")
  ))

developers ++= List(
  Developer(
    "hamnis",
    "Erlend Hamnaberg",
    "erlend@hamnaberg.net",
    new URL("https://twitter.com/hamnis")
  )
)
