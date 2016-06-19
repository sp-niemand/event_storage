package pro.codernumber1.event.actor

import akka.actor.{Actor, ActorLogging}

import pro.codernumber1.event.model.Event

class EventPersister extends Actor with ActorLogging {
  override def receive: Receive = {
    case e: Event => log.info(s"Event received: $e")
  }
}
