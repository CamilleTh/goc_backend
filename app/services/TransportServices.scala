package services

import javax.inject._
import play.modules.reactivemongo.{ReactiveMongoComponents, MongoController, ReactiveMongoApi}
import scala.concurrent.{Future, ExecutionContext}
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json.{JsObject, JsNumber, JsArray, Json}
import utils.Constants
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import reactivemongo.api.DefaultDB
import org.joda.time.DateTime

class TransportServices @Inject()(val reactiveMongoApi: ReactiveMongoApi,constant:Constants)(implicit exec: ExecutionContext) {

  def static_data: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("static-data"))
  def user_data: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("user-data"))

def getAroundRadars(lat:Double,lng:Double) = {
  for {
    radarsCol <- static_data
    radars_count <- radarsCol.count(Some(
      Json.obj(
        "type" -> constant.DATA_TRANSPORT,
        "subtype" -> constant.DATA_RADAR,
        "geometry" -> Json.obj(
          "$near" -> Json.obj(
            "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
            "$maxDistance" -> 1000
          )
      )))
    )
  } yield {
    radars_count
  }
}


  def getAroundAccidents(lat:Double,lng:Double) = {
    for {
      accidentsFuture <- static_data
      accidentsCount <- accidentsFuture.count(Some(
        Json.obj(
          "type" -> constant.DATA_TRANSPORT,
          "subtype" -> constant.DATA_ACCIDENT,
          "geometry" -> Json.obj(
            "$near" -> Json.obj(
              "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
              "$maxDistance" -> 1000
            )
          )))
      )
    } yield {
      accidentsCount
    }
  }

  def getAroundRoadHog(lat:Double,lng:Double, min_speed:Int = 130) = {
    for {
      userDataCol <- user_data
      roadHogCount <- userDataCol.count(Some(
        Json.obj(
          "type" -> constant.DATA_TRANSPORT,
          "subtype" -> constant.DATA_ROAD_HOG,
          "speed" -> Json.obj("$gte" -> min_speed),
          "date" -> Json.obj("$gte" -> Json.obj("$date" -> DateTime.now.minusMinutes(10).getMillis)),
          "geometry" -> Json.obj(
            "$near" -> Json.obj(
              "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
              "$maxDistance" -> 1000
            )
          )))
      )
    } yield {
      roadHogCount
    }
  }
  
}