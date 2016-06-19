package pro.codernumber1.event

import akka.actor.{ActorSystem, Props}
import pro.codernumber1.event.actor.{EventCounter, EventPersister}
import pro.codernumber1.event.front.HttpServer

object Main {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("main")

    val counter = actorSystem.actorOf(Props[EventCounter], "counter")
    val persister = actorSystem.actorOf(Props[EventPersister], "persister")

    new HttpServer(actorSystem, counter, persister).run()
  }
}
