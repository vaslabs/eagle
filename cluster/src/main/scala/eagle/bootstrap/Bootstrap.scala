package eagle.bootstrap

import akka.actor
import akka.actor.CoordinatedShutdown.JvmExitReason
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import cats.effect.{ExitCode, IO, IOApp, Resource}
import eagle.http.HttpRouter

import scala.concurrent.ExecutionContext
import cats.implicits._
import pureconfig._
import eagle.conf._
object Bootstrap extends IOApp{

  override def run(args: List[String]): IO[ExitCode] = {
    val systemAllocate: IO[ActorSystem] =
      IO(ActorSystem("Eagle"))

    import ExecutionContext.Implicits.global


    val systemRelease: ActorSystem => IO[Unit] = system =>
      IO.fromFuture(IO(CoordinatedShutdown(system).run(JvmExitReason))).void

    val allocateHttp: (ActorSystem, EagleConfig) => IO[ServerBinding] = (system, conf) => IO.fromFuture {
      implicit val actorSystem: ActorSystem = system
      implicit val actorMaterialiser: ActorMaterializer = ActorMaterializer()

      val router = new HttpRouter {}
      IO(Http().bindAndHandle(router.route, conf.http.bindInterface, conf.http.bindPort))
    }

    val releaseHttp: ServerBinding => IO[Unit] = serverBinding => IO.fromFuture(IO(serverBinding.unbind().map(_ => ())))

    def httpResource(actorSystem: ActorSystem, conf: EagleConfig): Resource[IO, ServerBinding] =
      Resource.make(allocateHttp(actorSystem, conf))(releaseHttp)


    val appResource: Resource[IO, (ActorSystem, EagleConfig)] = for {
      config <- Resource.liftF[IO, EagleConfig](IO(loadConfigOrThrow[EagleConfig]("eagle")))
      actorSystem <- Resource.make(systemAllocate)(systemRelease)
      httpBinding <- httpResource(actorSystem, config)
    } yield (actorSystem, config)

    appResource.use{
      case (actorSystem, eagleConfig) =>
         IO.never.map(_ => ExitCode.Success)
    }
  }

}
