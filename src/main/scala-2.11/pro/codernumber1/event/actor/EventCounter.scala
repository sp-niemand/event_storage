package pro.codernumber1.event.actor

import akka.actor.{Actor, ActorLogging}
import pro.codernumber1.event.model.Event

import scala.collection.mutable

class EventCounter extends Actor with ActorLogging {
  val counts: mutable.Map[String, Long] = mutable.Map.empty.withDefaultValue(0)

  override def receive: Receive = {
    case e: Event =>
      counts(e.`type`) = counts(e.`type`) + 1
      log.info(s"Counts now: $counts")
  }
}
