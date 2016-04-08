package services

import utils.GeoCodage._
import javax.inject._
import play.api.libs.ws._
import utils.{GeoCodageResponse, Constants}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{JsValue, JsObject, JsArray}

@Singleton
class GeoCodageService @Inject()(constants:Constants,ws: WSClient){

  def getCoordinatesFromAddress(address:String) = {

    val url = constants.GOOGLE_API_URL
    ws.url(url).withQueryString("key" -> constants.GOOGLE_API_KEY,"address" -> address).get map {
      response =>
        response.json.asOpt[GeoCodageResponse] flatMap {_.results.headOption.map(_.geometry.location)}
    }
  }

}
