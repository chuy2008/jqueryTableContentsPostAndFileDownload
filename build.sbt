
name := """jqueryTableContentsPostAndFileDownload"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.11" % "2.1.3" % "test",
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "org.squeryl" %% "squeryl" % "0.9.5-7",
    "mysql" % "mysql-connector-java" % "5.1.26",
    "org.apache.poi" % "poi" % "3.8",
    "org.apache.poi" % "poi-ooxml" % "3.9",
    jdbc,
    cache,
    ws
)

