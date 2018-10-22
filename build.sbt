sbtPlugin := true

name := "sbt-sonarrunner-plugin"

scalaVersion := "2.12.7"

organization := "damdev.sbt"

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-encoding", "UTF-8"
)

enablePlugins(SbtPlugin)

scriptedLaunchOpts := {
  scriptedLaunchOpts.value ++
    Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value)
}

scriptedBufferLog := false

libraryDependencies += "org.codehaus.sonar.runner" % "sonar-runner-dist" % "2.4"

publishMavenStyle := false

bintrayOrganization in bintray := None

bintrayPackageLabels := Seq("sbt", "sonar", "sbt-native-packager")

bintrayRepository := "sbt"

licenses +=("MIT", url("http://opensource.org/licenses/MIT"))
