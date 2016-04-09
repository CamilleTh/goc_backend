package models
import play.api.libs.json.Json

case class DigitalInternetResponse(city: String, UHD3: String);

object DigitalInternet {
  implicit val responseFormatter = Json.format[DigitalInternetResponse]
}