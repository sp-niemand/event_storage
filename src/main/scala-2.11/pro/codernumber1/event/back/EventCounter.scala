package pro.codernumber1.event.back

import akka.actor.{Actor, ActorLogging}
import pro.codernumber1.event.model.Event

class EventCounter extends Actor with ActorLogging {
  import EventCounter._
  var counters: Map[String, Long] = Map.empty

  override def receive: Receive = {
    case e: Event => counters = counters.updated(e.`type`, counters.get(e.`type`).map(_ + 1).getOrElse(1))
    case GetCounters => sender ! Counters(counters)
  }
}

object EventCounter {
  case object GetCounters
  case class Counters(data: Map[String, Long])
}