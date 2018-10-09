disablePlugins(aether.AetherPlugin)
enablePlugins(aether.SignedAetherPlugin)

overridePublishSignedSettings
overridePublishLocalSettings

publishTo := {
  if (isSnapshot.value) {
    Some(Opts.resolver.sonatypeSnapshots)
  } else {
    Some(Opts.resolver.sonatypeStaging)
  }
}

pomIncludeRepository := { x =>
  false
}

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
    "Implementation-Vendor"    -> vendor
  )
}

credentials ++= Seq(
  Credentials(Path.userHome / ".sbt" / ".credentials"),
)

homepage := Some(url("https://github.com/hamnis/arities"))

startYear := Some(2017)

licenses := Seq(
  "Apache2" -> url("https://github.com/hamnis/arities/blob/master/LICENSE.txt")
)

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ =>
  false
}

releaseCrossBuild := true

releasePublishArtifactsAction := PgpKeys.publishSigned.value

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
