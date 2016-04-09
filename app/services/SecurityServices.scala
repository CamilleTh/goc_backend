package services

import javax.inject._
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{Future, ExecutionContext}
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json.{JsNumber, Json, JsArray}
import utils.Constants

class SecurityServices @Inject()(val reactiveMongoApi: ReactiveMongoApi,constant:Constants)(implicit exec: ExecutionContext) {

  def static_data: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("static-data"))

def getAroundCrimes(lat:Double,lng:Double) = {
  for {
    crimesCol <- static_data
    crimes_count <- crimesCol.count(Some(
      Json.obj(
        "type" -> constant.DATA_SECURITY,
        "subtype" -> constant.DATA_CRIMES,
        "geometry" -> Json.obj(
          "$near" -> Json.obj(
            "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
            "$maxDistance" -> 1000
          )
      )))
    )
  } yield {
    crimes_count
  }
}
}