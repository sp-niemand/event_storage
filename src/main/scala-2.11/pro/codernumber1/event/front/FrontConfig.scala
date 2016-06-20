package pro.codernumber1.event.front

import akka.actor.{ExtendedActorSystem, ExtensionKey}
import net.ceedubs.ficus.Ficus._
import pro.codernumber1.event.util.BaseConfig

import scala.concurrent.duration._

final class FrontConfig(system: ExtendedActorSystem) extends BaseConfig(system) {
  override protected val prefix: String = "front"
  val counterAskTimeout: FiniteDuration = getOrElse("counters-ask-timeout", 1 second)
  val interface: String = getOrElse("interface", "localhost")
  val port: Int = getOrElse("port", 8080)
}

object FrontConfig extends ExtensionKey[FrontConfig]
