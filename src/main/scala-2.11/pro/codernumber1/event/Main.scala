package pro.codernumber1.event

import akka.actor.ActorSystem
import pro.codernumber1.event.front.HttpServer

object Main {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("main")
    new HttpServer(actorSystem).run()
  }
}
