package models

import play.api.libs.json.Json

case class GeoCodageResponse(results: List[GeoCodageGeometry])

case class GeoCodageGeometry(geometry: GeoCodageLocation, address_components: List[GeoAddressComponents])

case class GeoCodageLocation(location: Coordinates)

case class GeoAddressComponents(long_name: String, short_name: String, types: List[String])

object GeoCodage {
  implicit val locationFormatter = Json.format[GeoCodageLocation]
  implicit val addressComponentsFormatter = Json.format[GeoAddressComponents]
  implicit val geometryFormatter = Json.format[GeoCodageGeometry]
  implicit val responseFormatter = Json.format[GeoCodageResponse]
}