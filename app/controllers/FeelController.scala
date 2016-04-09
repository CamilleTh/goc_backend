package controllers

import javax.inject._

import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import scala.concurrent.{ ExecutionContext, Future }
import services.GeoCodageService
import services.WeatherServices;

/**
 * Simple controller that directly stores and retrieves [models.City] instances into a MongoDB Collection
 * Input is first converted into a city and then the city is converted to JsObject to be stored in MongoDB
 */
@Singleton
class FeelController @Inject() (val reactiveMongoApi: ReactiveMongoApi, val geoCodageService: GeoCodageService, val weatherServices: WeatherServices)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents {

  def transport(lat: Long, lng: Long) = Action.async {
    geoCodageService.getCoordinatesFromAddress("OTTANGE") map println
    Future.successful(Ok(JsNumber((Math.random() * 100.toInt))))
  }

  def security(lat: Long, lng: Long) = Action.async {
    Future.successful(Ok(JsNumber((Math.random() * 100.toInt))))
  }

  def digital(lat: Long, lng: Long) = Action.async {
    Future.successful(Ok(JsNumber((Math.random() * 100.toInt))))
  }

  def health(lat: Long, lng: Long) = Action.async {
    Future.successful(Ok(JsNumber((Math.random() * 100.toInt))))
  }

  def weather(lat: Long, lng: Long) = Action.async {
    Future.successful(Ok(JsNumber((Math.random() * 100).toInt)))
  }

  def weather(lat: Long, lng: Long) = Action.async {
    val percentOpenWeather: Future[Option[Int]] = weatherServices.getWeatherFeelingFromOpenWeather(lat, lng);
    percentOpenWeather.map { optionPercent =>
      optionPercent match {
        case Some(x) =>
          println("Some" + x); Ok(JsNumber(x))
        case None    => println("None"); Ok(JsNumber(0))
      }
    }
  }
}


