package com.aol.sbt.sonar

import java.io.File
import java.util.Properties

import org.sonarsource.scanner.api.{EmbeddedScanner, LogOutput}
import sbt.Keys._
import sbt._

import scala.collection.JavaConversions
;

object SonarRunnerPlugin extends AutoPlugin {

  class SonarLogger(logger: Logger) extends LogOutput {
    override def log(formattedMessage: String, level: LogOutput.Level): Unit = {
      level match {
        case LogOutput.Level.TRACE => logger.debug(formattedMessage)
        case LogOutput.Level.DEBUG => logger.debug(formattedMessage)
        case LogOutput.Level.INFO => logger.info(formattedMessage)
        case LogOutput.Level.WARN => logger.warn(formattedMessage)
        case LogOutput.Level.ERROR => logger.error(formattedMessage)
      }
    }
  }

  object autoImport {
    val sonarProperties = settingKey[Map[String, String]]("SonarRunner configuration properties. See http://docs.codehaus.org/display/SONAR/Analysis+Parameters.")
    val sonar = taskKey[Unit]("Runs Sonar agent")
    val sonarRunnerOptions = settingKey[Seq[String]]("Extra options for sonar runner")
  }

  import com.aol.sbt.sonar.SonarRunnerPlugin.autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    sonarProperties := Map(
      "sonar.projectName" -> name.value,
      "sonar.projectVersion" -> version.value,
      "sonar.projectKey" -> "%s:%s".format(organization.value, name.value),
      "sonar.java.binaries" -> filePathsToString(Seq((classDirectory in Compile).value)),
      "sonar.sources" -> filePathsToString((unmanagedSourceDirectories in Compile).value),
      "sonar.tests" -> filePathsToString((unmanagedSourceDirectories in Test).value),
      "sonar.projectBaseDir" -> file(".").absolutePath,
      "sonar.sourceEncoding" -> "UTF-8"
    ),
    sonarRunnerOptions := Seq.empty,
    sonar := {
      lazy val logger: TaskStreams = streams.value
      runSonarAgent(mapToProperties(sonarProperties.value), logger, sbtVersion.value)
    }
  )

  def runSonarAgent(props: Properties, logger: TaskStreams, sbtVersion: String) = {
    val scanner = EmbeddedScanner.create(new SonarLogger(logger.log)).setApp("ScannerSBT", s"0.1/$sbtVersion").addGlobalProperties(props)
    scanner.start()
    scanner.runAnalysis(props)
    scanner.stop()
  }

  private[this] def mapToProperties(props: Map[String, String]): Properties = {
    val p = new Properties()
    p.putAll(JavaConversions.mapAsJavaMap(props))
    p
  }

  private[this] def filePathsToString(files: Seq[File]) = files.filter(_.exists).map(_.getAbsolutePath).toSet.mkString(",")
}