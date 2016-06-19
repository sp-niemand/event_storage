package pro.codernumber1.event.front

import pro.codernumber1.event.model.Event
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

object JsonProtocol extends DefaultJsonProtocol {
  implicit object EventJsonFormat extends RootJsonFormat[Event] {
    override def write(obj: Event): JsValue =
      JsObject(
        "event_type" -> JsString(obj.`type`),
        "ts" -> JsNumber(obj.timestamp),
        "params" -> JsObject(obj.properties)
      )

    override def read(json: JsValue): Event =
      json.asJsObject.getFields("event_type", "ts", "params") match {
        case Seq(JsString(eventType), JsNumber(ts), params) =>
          Event(eventType, ts.toLong, params.asJsObject.convertTo[Map[String, JsValue]])
        case _ => throw new DeserializationException("Color expected")
      }
  }
}
