package pro.codernumber1.event.actor

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import akka.actor.{Actor, ActorLogging}
import org.mongodb.scala.bson._
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import pro.codernumber1.event.back.BackConfig
import pro.codernumber1.event.model.Event
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class EventPersister extends Actor with ActorLogging {
  import EventPersister._
  import context.dispatcher

  private val config = BackConfig(context.system)
  private val client: MongoClient = MongoClient(config.mongoClientUri)
  private val db: MongoDatabase = client.getDatabase(config.mongoDb)

  private var collectionCache: Option[CollectionCache] = None
  private def collection: MCollection = {
    val now = LocalDateTime.now()
    val day = now.getDayOfYear
    if (!collectionCache.exists(_.day == now.getDayOfYear)) {
      collectionCache = Some(CollectionCache(
        day,
        db.getCollection(now.format(DateTimeFormatter.ISO_LOCAL_DATE))
      ))
    }
    collectionCache.get.collection
  }

  override def receive: Receive = {
    case event: Event =>
      log.debug(s"Event received: $event")
      val document = Try(Document(
        "ts" -> event.timestamp,
        "t" -> event.`type`,
        "p" -> Document(event.properties.toJson.compactPrint)
      ))
      Future.fromTry(document).map(collection.insertOne(_).head()) onComplete {
        case Success(_) => log.debug(s"Event persisted: $event")
        case Failure(t) => log.error(t, "Error while persisting event")
      }
  }
}

object EventPersister {
  type MCollection = MongoCollection[Document]
  case class CollectionCache(day: Int, collection: MCollection)
}