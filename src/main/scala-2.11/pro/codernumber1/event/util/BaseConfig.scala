package pro.codernumber1.event.util

import akka.actor.{ExtendedActorSystem, Extension}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ValueReader

abstract class BaseConfig(system: ExtendedActorSystem) extends Extension {
  private val config = system.settings.config

  protected val prefix: String

  protected def getOrElse[A](path: String, default: => A)(implicit reader: ValueReader[Option[A]]): A =
    config.getOrElse[A](s"$prefix.$path", default)(reader)
}
