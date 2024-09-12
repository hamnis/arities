homepage := Some(url("https://github.com/hamnis/arities"))
startYear := Some(2018)
licenses := Seq(License.Apache2)
scmInfo := Some(
  ScmInfo(
    new URL("https://github.com/hamnis/arities"),
    "scm:git:git@github.com:hamnis/arities.git",
    Some("scm:git:git@github.com:hamnis/arities.git")
  ))
javacOptions ++= List("--release", "17")
tlSonatypeUseLegacyHost := true
ThisBuild / githubWorkflowJavaVersions := List(JavaSpec.temurin("17"))

developers := List(
  tlGitHubDev("hamnis", "Erlend Hamnaberg")
)
