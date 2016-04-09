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

@Singleton
class FeelController @Inject()(val reactiveMongoApi: ReactiveMongoApi,val geoCodageService:GeoCodageService, val transportServices:TransportServices, val weatherServices:WeatherServices, val securityServices:SecurityServices)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents {

  def transport(lat: Double, lng: Double) = Action.async{
    for {
      aroundRadar <- transportServices.getAroundRadars(lat,lng)
      aroundAccident <- transportServices.getAroundAccidents(lat,lng)
    } yield {
      println(aroundRadar)
      println(aroundAccident)
      Ok(JsNumber(100 + aroundRadar - aroundAccident))
    }
  }

  def security(lat: Double, lng: Double) = Action.async{
    securityServices.getAroundCrimes(lat,lng) map { res =>
      Ok(JsNumber(100 - res))
    }
  }

  def digital(lat: Double, lng: Double) = Action.async{
   Future.successful(Ok(JsNumber((Math.random()*100).toInt)))
  }

  def health(lat: Double, lng: Double) = Action.async{
    Future.successful(Ok(JsNumber((Math.random()*100).toInt)))
  }

  def weather(lat: Double, lng: Double) = Action.async {
    val percentOpenWeather: Future[Option[Int]] = weatherServices.getWeatherFeelingFromOpenWeather(lat, lng);
    percentOpenWeather.map { optionPercent =>
      optionPercent match {
        case Some(x) =>
          println("Some" + x); Ok(JsNumber(x))
        case None    => println("None"); Ok(JsNumber(0))
      }
    }
  }

  def up(category:String) = Action.async{
    Future.successful(Ok(category))
  }
}


