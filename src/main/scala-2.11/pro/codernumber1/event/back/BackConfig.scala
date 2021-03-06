package pro.codernumber1.event.back

import akka.actor.{ExtendedActorSystem, ExtensionKey}
import net.ceedubs.ficus.Ficus._
import pro.codernumber1.event.util.BaseConfig

final class BackConfig(system: ExtendedActorSystem) extends BaseConfig(system) {
  override protected val prefix: String = "back"
  val mongoClientUri: String = getOrElse("mongo-client-uri", "mongodb://localhost:27017")
  val mongoDb: String = getOrElse("mongo-db", "admin")
}

object BackConfig extends ExtensionKey[BackConfig]