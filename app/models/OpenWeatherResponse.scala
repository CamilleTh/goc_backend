package models

import play.api.libs.json.Json

case class OpenWeatherResponse(coord: OpenWeatherCoordinates, weather: List[OpenWeatherWeather])

case class OpenWeatherCoordinates(lat: Double, lon: Double)

case class OpenWeatherWeather(id: Long, main: String, description: String, icon: String);

object OpenWeather {
  implicit val coordinatesFormatter = Json.format[OpenWeatherCoordinates]
  implicit val weatherFormatter = Json.format[OpenWeatherWeather]
  implicit val responseFormatter = Json.format[OpenWeatherResponse]
}