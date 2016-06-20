package pro.codernumber1.event.front

import akka.actor.{ExtendedActorSystem, Extension, ExtensionKey}
import net.ceedubs.ficus.Ficus._

import scala.concurrent.duration._

final class FrontConfig(system: ExtendedActorSystem) extends Extension {
  private val config = system.settings.config
  private val prefix = "front"
  val counterAskTimeout: FiniteDuration =
    config.getOrElse(s"$prefix.counters-ask-timeout", 1 second)
  val interface: String = config.getOrElse(s"$prefix.interface", "localhost")
  val port: Int = config.getOrElse(s"$prefix.port", 8080)
}

object FrontConfig extends ExtensionKey[FrontConfig]
