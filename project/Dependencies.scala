import sbt._

object Dependencies {

  object Versions {
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
    object Akka {
      val actor = "com.typesafe.akka" %% "akka-actor" % Versions.akka
      val testKit = "com.typesafe.akka" %% "akka-testkit" % Versions.akka % Test
      val http = "com.typesafe.akka" %% "akka-http" % Versions.akkaHttp
      val httpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttp % Test
      val cluster = "com.typesafe.akka" %% "akka-cluster" % Versions.akka
      val clusterSharding = "com.typesafe.akka" %% "akka-cluster-sharding" %  Versions.akka
      val persistence =  "com.typesafe.akka" %% "akka-persistence" % Versions.akka

      object Discovery {
        val kubernetes = "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % Versions.akkaDiscovery
      }

      val all = Seq(actor, testKit, http, httpTestkit, cluster, clusterSharding, persistence, Discovery.kubernetes)
    }

  }

  import Libraries._
  lazy val eagleCluster = Akka.all ++ cats.all
}
