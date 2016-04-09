package services

import javax.inject._
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{Future, ExecutionContext}
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json.{JsNumber, JsArray, Json}
import utils.Constants
import org.joda.time.DateTime
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
class FeelServices @Inject()(val reactiveMongoApi: ReactiveMongoApi,constant:Constants)(implicit exec: ExecutionContext) {

  def feelings: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("feeling"))
  def user_data: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("user-data"))

  def getFeelings(lat:Double,lng:Double,feeling_type:String) = {
    for {
      feelingsCol <- feelings
      good_feelings_count <- feelingsCol.count(Some(
        Json.obj(
          "feeling" -> constant.GOOD_FEELING,
          "date" -> Json.obj("$gte" -> Json.obj("$date" -> DateTime.now.minusHours(1).getMillis)),
          "type" -> feeling_type,
          "geometry" -> Json.obj(
            "$near" -> Json.obj(
              "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
              "$maxDistance" -> 1000
            )
        )))
      )
      bad_feelings_count <- feelingsCol.count(Some(
        Json.obj(
          "feeling" -> constant.BAD_FEELING,
          "date" -> Json.obj("$gte" -> Json.obj("$date" -> DateTime.now.minusHours(1).getMillis)),
          "type" -> feeling_type,
          "geometry" -> Json.obj(
            "$near" -> Json.obj(
              "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
              "$maxDistance" -> 1000
            )
          )))
      )
    } yield {
      good_feelings_count + bad_feelings_count
    }
  }

  def postBadFeeling(lat:Double,lng:Double,feeling_type:String) = {
    for {
      feelingsCol <- feelings
      last_error <- feelingsCol.insert(
        Json.obj(
          "feeling" -> constant.BAD_FEELING,
          "date" -> Json.obj("$date" -> DateTime.now.getMillis),
          "type" -> feeling_type,
          "geometry" -> Json.obj(
            "$near" -> Json.obj(
              "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
              "$maxDistance" -> 1000
            )
          ))
      )
    } yield {
      last_error.ok
    }
  }

  def postGoodFeeling(lat:Double,lng:Double,feeling_type:String) = {
    for {
      feelingsCol <- feelings
      last_error <- feelingsCol.insert(
        Json.obj(
          "feeling" -> constant.GOOD_FEELING,
          "date" -> Json.obj("$date" -> DateTime.now.getMillis),
          "type" -> feeling_type,
          "geometry" -> Json.obj(
            "$near" -> Json.obj(
              "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
              "$maxDistance" -> 1000
            )
          ))
      )
    } yield {
      last_error.ok
    }
  }
  def addSpeed(lat:Double,lng:Double,speed:Int) = {
    for {
      userDataCol <- user_data
      last_error <- userDataCol.insert(
        Json.obj(
          "type" -> constant.DATA_TRANSPORT,
          "subtype" -> constant.DATA_ROAD_HOG,
          "speed" -> speed,
          "date" -> Json.obj("$date" -> DateTime.now.getMillis),
          "geometry" -> Json.obj(
            "$near" -> Json.obj(
              "$geometry" -> Json.obj("type" -> "Point", "coordinates" -> JsArray(Seq(JsNumber(lat),JsNumber(lng)))),
              "$maxDistance" -> 1000
            )
          ))
      )
    } yield {
      last_error.ok
    }
  }

}