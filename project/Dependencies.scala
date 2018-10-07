import sbt._

object Dependencies {

  object Versions {
    val akkaCirceSupport: String = "1.22.0"


    val circe: String = "0.9.3"

    val scalatest: String = "3.0.5"
    val akka = "2.5.16"
    val akkaHttp = "10.1.5"
    val akkaDiscovery = "0.18.0"
    val catsEffect = "1.0.0"
    val cats = "1.4.0"
  }

  object Libraries {

    object cats {
      val core = "org.typelevel" %% "cats-core" % Versions.cats
      val effect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
      val all = Seq(core, effect)
    }
    object Circe {
      val core = "io.circe" %% "circe-core" % Versions.circe
      val generic = "io.circe" %% "circe-generic" % Versions.circe
      val parser = "io.circe" %% "circe-parser" % Versions.circe
      val all = Seq(core, generic, parser)
    }
    object Akka {
      val actor = "com.typesafe.akka" %% "akka-actor" % Versions.akka
      val testKit = "com.typesafe.akka" %% "akka-testkit" % Versions.akka % Test
      val http = "com.typesafe.akka" %% "akka-http" % Versions.akkaHttp
      val httpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttp % Test
      val cluster = "com.typesafe.akka" %% "akka-cluster" % Versions.akka
      val clusterSharding = "com.typesafe.akka" %% "akka-cluster-sharding" %  Versions.akka
      val persistence =  "com.typesafe.akka" %% "akka-persistence" % Versions.akka
      val circeSupport = "de.heikoseeberger" %% "akka-http-circe" % Versions.akkaCirceSupport

      object Discovery {
        val kubernetes = "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % Versions.akkaDiscovery
      }

      val all = Seq(actor, testKit, http, httpTestkit, cluster, clusterSharding, persistence, Discovery.kubernetes, circeSupport)
    }

    object ScalaTest {
      val scalatest = "org.scalatest" %% "scalatest" % Versions.scalatest % Test
    }
    
    val pureConf = "com.github.pureconfig" %% "pureconfig" % "0.9.2"

  }

  import Libraries._
  lazy val eagleCluster = Akka.all ++ cats.all ++ Seq(pureConf,  ScalaTest.scalatest) ++ Circe.all
}
