package services

import models.GeoCodage._
import javax.inject._
import play.api.libs.ws._
import utils.{ Constants }
import models.GeoCodageResponse
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{ JsValue, JsObject, JsArray }

@Singleton
class GeoCodageService @Inject() (constants: Constants, ws: WSClient) {

  def getCoordinatesFromAddress(address: String) = {

    val url = constants.GOOGLE_API_URL
    ws.url(url).withQueryString("key" -> constants.GOOGLE_API_KEY, "address" -> address).get map {
      response =>
        response.json.asOpt[GeoCodageResponse] flatMap { _.results.headOption.map(_.geometry.location) }
    }
  }

  def getCityFromCoordinates(lat: Double, lng: Double) = {

    val url = constants.GOOGLE_API_URL
    ws.url(url).withQueryString("key" -> constants.GOOGLE_API_KEY, "latlng" -> (lat.toString + "," + lng.toString)).get map {
      response =>
        response.json.asOpt[GeoCodageResponse] flatMap { _.results.headOption.map(_.address_components.filter { compo => compo.types.contains(constants.GEO_TYPE_CITY_CODE) }.head.long_name) }
    }
  }

}
