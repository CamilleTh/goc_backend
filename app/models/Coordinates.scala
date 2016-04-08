package models

import play.api.libs.json.Json

case class Coordinates(lat:Double,lng:Double)

object Coordinates {
  implicit val formatter = Json.format[Coordinates]
}
