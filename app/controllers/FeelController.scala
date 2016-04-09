package controllers

import javax.inject._
import play.api.cache._
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import scala.concurrent.{ ExecutionContext, Future }
import services._
import scala.Some
import play.api.libs.json.JsNumber
import utils.Constants
import models.Coordinates
import models.Coordinates._

@Singleton
class FeelController @Inject()(cache: CacheApi,val reactiveMongoApi: ReactiveMongoApi,val constants:Constants, feelServices:FeelServices, val geoCodageService:GeoCodageService, val transportServices:TransportServices, val weatherServices:WeatherServices, val securityServices:SecurityServices, val digitalService : DigitalService)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents {


  def transport(lat: Double, lng: Double) = Action.async {
    for {
      aroundRadar <- transportServices.getAroundRadars(lat,lng)
      aroundAccident <- transportServices.getAroundAccidents(lat,lng)
      aroundRoadHog <- transportServices.getAroundRoadHog(lat,lng)
      transportFeelings <- feelServices.getFeelings(lat,lng,constants.DATA_TRANSPORT)
    } yield {
      Ok(JsNumber(100 + aroundRadar - aroundAccident + transportFeelings * 10 - aroundRoadHog))
    }
  }

  def security(lat: Double, lng: Double) = Action.async{
    for {
      aroundCrimes <- securityServices.getAroundCrimes(lat,lng)
      securityFeelings <- feelServices.getFeelings(lat,lng,constants.DATA_SECURITY)
    } yield {
      Ok(JsNumber(100 - aroundCrimes + securityFeelings * 10))
    }
  }

  def digital(lat: Double, lng: Double) = Action.async {
    for {
      uHD3 <- digitalService.getUHD3(lat, lng)
    } yield {
      uHD3 match {
        case Some(x) =>
          println("SomeDigital : " + x); Ok(JsNumber(x.UHD3.replace("%", "").toDouble))
        case None => println("NoneDigital"); Ok(JsNumber(0))
      }
    }
  }

  def health(lat: Double, lng: Double) = Action.async {
    val health = cache.get[Int]("health") match {
      case Some(h) => h
      case None => (Math.random() * 100).toInt
    }
    val newhealth = health > 100 match {
      case true => 0
      case false => health+1
    }
    cache.set("health", newhealth)
    println(newhealth)
    Future.successful(Ok(JsNumber(newhealth)))
  }

  def weather(lat: Double, lng: Double) = Action.async {
    for {
      percentOpenWeatherOpt <- weatherServices.getWeatherFeelingFromOpenWeather(lat, lng)
      weatherFeelings <- feelServices.getFeelings(lat,lng,constants.DATA_WEATHER)
    } yield {
      Ok(JsNumber(percentOpenWeatherOpt.getOrElse(0) + weatherFeelings * 10))
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

  def addSpeed(speed:Int) = Action.async { request =>
    request.body.asJson.flatMap{ jsonBody =>
      jsonBody.asOpt[Coordinates] map { coordinate =>
        feelServices.addSpeed(coordinate.lat,coordinate.lng,speed) map {
          case true => Ok(JsNumber(speed))
          case false => InternalServerError
        }
      }
    }.getOrElse(Future.successful(BadRequest))
  }

}


