import Dependencies._

ThisBuild / organization := "org.vaslabs"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.6"



lazy val defaultDockerSettings = {
  packageName in Docker := "org.vaslabs.eagle"
  version in Docker := version.value
  maintainer in Docker := "vaslabs"
  dockerBaseImage in Docker := "openjdk:8u171-jdk-alpine3.8"
}

lazy val cluster = (project in file("cluster"))
  .settings(
    name := "eagle-cluster",
    libraryDependencies ++= eagleCluster
  ).enablePlugins(DockerPlugin)
  .settings(defaultDockerSettings)

