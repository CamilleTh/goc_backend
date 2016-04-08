package utils

import models.Coordinates
import play.api.libs.json.Json

case class GeoCodageResponse(results:List[GeoCodageGeometry])

case class GeoCodageGeometry(geometry:GeoCodageLocation)

case class GeoCodageLocation(location:Coordinates)


object GeoCodage {
  implicit val locationFormatter = Json.format[GeoCodageLocation]
  implicit val geometryFormatter = Json.format[GeoCodageGeometry]
  implicit val responseFormatter = Json.format[GeoCodageResponse]
}