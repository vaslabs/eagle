package eagle.bootstrap

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import cats.effect.{ExitCode, IO, IOApp, Resource}
import eagle.http.HttpRouter

import scala.concurrent.ExecutionContext
import cats.implicits._

object Bootstrap extends IOApp{

  override def run(args: List[String]): IO[ExitCode] = {
    val systemAllocate: IO[ActorSystem] =
      IO(ActorSystem("Eagle"))

    import ExecutionContext.Implicits.global


    val systemRelease: ActorSystem => IO[Unit] = system => IO.fromFuture(IO(system.terminate().map(_ => ())))

    val allocateHttp: (ActorSystem, ActorMaterializer) => IO[ServerBinding] = (system, mat) => IO.fromFuture {
      implicit val actorMaterialiser: ActorMaterializer = mat
      implicit val actorSystem: ActorSystem = system
      val router = new HttpRouter {}
      IO(Http().bindAndHandle(router.route, "0.0.0.0", 8080))
    }

    val releaseHttp: ServerBinding => IO[Unit] = serverBinding => IO.fromFuture(IO(serverBinding.unbind().map(_ => ())))

    def httpResource(actorSystem: ActorSystem): Resource[IO, ServerBinding] =
      Resource.make(allocateHttp(actorSystem, ActorMaterializer()(actorSystem)))(releaseHttp)


    val appResource: Resource[IO, ActorSystem] = for {
      actorSystem <- Resource.make(systemAllocate)(systemRelease)
      httpBinding <- httpResource(actorSystem)
    } yield actorSystem

    appResource.use(_ => IO.never.map(_ => ExitCode.Success))
  }

}
