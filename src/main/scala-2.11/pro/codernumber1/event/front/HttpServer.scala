package pro.codernumber1.event.front

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.io.StdIn

class HttpServer(actorSystem: ActorSystem, interface: String = "localhost", port: Int = 8080) {
  def run(): Unit = {
    implicit val system = actorSystem
    implicit val executionContext = system.dispatcher
    implicit val materializer = ActorMaterializer()

    val route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      }

    val f = Http().bindAndHandle(Route.handlerFlow(route), interface, port)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    f.flatMap(_.unbind())
  }
}