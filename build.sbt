name := """task-list-REST"""
organization := "com.tasklistrest"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean, LauncherJarPlugin)

scalaVersion := "2.13.8"

libraryDependencies += "org.projectlombok" % "lombok" % "1.18.22"
libraryDependencies += "com.h2database" % "h2" % "1.4.199"
libraryDependencies += "commons-validator" % "commons-validator" % "1.7"
libraryDependencies ++= Seq(
  guice,
  evolutions,
  javaJdbc
)
