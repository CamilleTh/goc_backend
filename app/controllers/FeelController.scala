package controllers

import javax.inject._

import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import scala.concurrent.{ ExecutionContext, Future }
import services._

import scala.concurrent.{ExecutionContext, Future}
import scala.Some
import play.api.libs.json.JsNumber
import utils.Constants
import models.Coordinates
import models.Coordinates._

@Singleton
class FeelController @Inject()(val reactiveMongoApi: ReactiveMongoApi,val constants:Constants, feelServices:FeelServices, val geoCodageService:GeoCodageService, val transportServices:TransportServices, val weatherServices:WeatherServices, val securityServices:SecurityServices)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents {

  def transport(lat: Double, lng: Double) = Action.async{
    for {
      aroundRadar <- transportServices.getAroundRadars(lat,lng)
      aroundAccident <- transportServices.getAroundAccidents(lat,lng)
      transportFeelings <- feelServices.getFeelings(lat,lng,constants.DATA_TRANSPORT)
    } yield {
      Ok(JsNumber(100 + aroundRadar - aroundAccident + transportFeelings))
    }
  }

  def security(lat: Double, lng: Double) = Action.async{
    for {
      aroundCrimes <- securityServices.getAroundCrimes(lat,lng)
      securityFeelings <- feelServices.getFeelings(lat,lng,constants.DATA_SECURITY)
    } yield {
      Ok(JsNumber(100 - aroundCrimes + securityFeelings))
    }
  }

  def digital(lat: Double, lng: Double) = Action.async{
   Future.successful(Ok(JsNumber((Math.random()*100).toInt)))
  }

  def health(lat: Double, lng: Double) = Action.async{
    Future.successful(Ok(JsNumber((Math.random()*100).toInt)))
  }

  def weather(lat: Double, lng: Double) = Action.async {
    for {
      percentOpenWeatherOpt <- weatherServices.getWeatherFeelingFromOpenWeather(lat, lng)
      weatherFeelings <- feelServices.getFeelings(lat,lng,constants.DATA_WEATHER)
    } yield {
      Ok(JsNumber(percentOpenWeatherOpt.getOrElse(0) + weatherFeelings))
    }
  }

  def up(category:String) = Action.async{ request =>
    request.body.asJson.flatMap{ jsonBody =>
      jsonBody.asOpt[Coordinates] map { coordinate =>
        feelServices.postGoodFeeling(coordinate.lat,coordinate.lng,category) map {
          case true => Ok(category)
          case false => InternalServerError
        }
      }
    }.getOrElse(Future.successful(BadRequest))
  }

  def down(category:String) = Action.async{ request =>
      request.body.asJson.flatMap{ jsonBody =>
        jsonBody.asOpt[Coordinates] map { coordinate =>
          feelServices.postBadFeeling(coordinate.lat,coordinate.lng,category) map {
            case true => Ok(category)
            case false => InternalServerError
          }
        }
      }.getOrElse(Future.successful(BadRequest))
    }

}


