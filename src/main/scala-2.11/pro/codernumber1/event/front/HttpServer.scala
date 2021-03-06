package pro.codernumber1.event.front

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route, StandardRoute}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import pro.codernumber1.event.back.EventCounter.{Counters, GetCounters}
import pro.codernumber1.event.front.JsonProtocol._
import pro.codernumber1.event.model.Event
import spray.json._

import scala.util.{Failure, Success}

class HttpServer(
                  actorSystem: ActorSystem,
                  counter: ActorRef,
                  persister: ActorRef
                ) extends SprayJsonSupport {

  val settings = FrontConfig(actorSystem)
  val interface = settings.interface
  val port = settings.port

  val log = Logging(actorSystem, this.getClass.getCanonicalName)

  private def completeWithJson[T](
                           data: T,
                           code: StatusCode = StatusCodes.OK
                         )(implicit writer: JsonWriter[T]): StandardRoute =
    complete(HttpEntity(ContentTypes.`application/json`, data.toJson(writer).compactPrint))

  implicit val myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case t =>
        extractUri { uri =>
          log.error(t, s"Request to $uri could not be handled normally")
          complete(HttpResponse(StatusCodes.InternalServerError))
        }
    }

  def run(): Unit = {
    implicit val system = actorSystem
    implicit val executionContext = system.dispatcher
    implicit val materializer = ActorMaterializer()

    val route =
      pathPrefix("event") {
        path("count") {
          get {
            onComplete(counter.ask(GetCounters)(settings.counterAskTimeout)) {
              case Success(Counters(data)) => completeWithJson(data)
              case Success(m) => failWith(new RuntimeException(s"Wrong message received from counter actor: $m"))
              case Failure(t) => failWith(t)
            }
          }
        } ~ post {
          entity(as[Event]) { event =>
            counter ! event
            persister ! event
            completeWithJson(event)
          }
        }
      }

    val f = Http().bindAndHandle(Route.handlerFlow(route), interface, port)
    f onComplete {
      case _: Success[_] => log.info(s"Server online on interface $interface, port $port")
      case Failure(t) => log.error(t, "HTTP server binding failed")
    }
  }
}