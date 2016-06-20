package pro.codernumber1.event.model

import spray.json.JsValue

case class Event(`type`: String, timestamp: Long, properties: Map[String, JsValue])