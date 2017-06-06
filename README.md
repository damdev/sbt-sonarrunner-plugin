# sbt-sonarrunner-plugin
An SBT plugin to publish code quality data to SonarQube

Build status
------------

![Build health](https://travis-ci.org/aol/sbt-sonarrunner-plugin.svg)


Installation
------------

NOTE this plugin targets sbt 0.13.6+
=================

Add the following to your `project/plugins.sbt` file:

```scala
addSbtPlugin("com.aol.sbt" % "sbt-sonarrunner-plugin" % "1.0.4")
```

To use the SonarRunner settings in your project, add the `SonarRunnerPlugin` auto-plugin to your project.

```scala
enablePlugins(SonarRunnerPlugin)
```


Configuration
-------------

To use specific Sonar settings, add the following to your `build.sbt` file:

```scala
sonarProperties ++= Map(
      "sonar.host.url" -> "http://sonarhostname.com",
      "sonar.jdbc.username" -> "sonar",
      "sonar.jdbc.password" -> "sonar",
      "sonar.coverage.exclusions" -> "**/MobileAppController.java,**/SomeClass.java"
    )
```

Run Sonar

```bash
    sbt sonar
```

Full list of Sonar analysis parameters
--------------------------------------
http://docs.sonarqube.org/display/SONAR/Analysis+Parameters

Bintray
--------------------------------------
https://bintray.com/aol/scala/sbt-sonarrunner-plugin/view

Author
--------------------------------------
maestr0 - Pawel Raszewski
