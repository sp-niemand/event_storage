package pro.codernumber1.event.front

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import pro.codernumber1.event.model.Event

import scala.io.StdIn

import spray.json._
import JsonProtocol._

class HttpServer(actorSystem: ActorSystem, interface: String = "localhost", port: Int = 8080) extends SprayJsonSupport {
  def run(): Unit = {
    implicit val system = actorSystem
    implicit val executionContext = system.dispatcher
    implicit val materializer = ActorMaterializer()

    val route =
      pathPrefix("event") {
        path("count") {
          get {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>event get</h1>"))
          }
        } ~ post {
          entity(as[Event]) { event =>
            complete(HttpEntity(ContentTypes.`application/json`, event.toJson.prettyPrint))
          }
        }
      }

    val f = Http().bindAndHandle(Route.handlerFlow(route), interface, port)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    f.flatMap(_.unbind())
  }
}