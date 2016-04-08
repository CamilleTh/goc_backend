package controllers

import javax.inject._

import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import scala.concurrent.{ExecutionContext, Future}
import services.GeoCodageService


/**
 * Simple controller that directly stores and retrieves [models.City] instances into a MongoDB Collection
 * Input is first converted into a city and then the city is converted to JsObject to be stored in MongoDB
 */
@Singleton
class FeelController @Inject()(val reactiveMongoApi: ReactiveMongoApi,val geoCodageService:GeoCodageService)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents {


  def transport(lat: Long, lng: Long) = Action.async{
    geoCodageService.getCoordinatesFromAddress("OTTANGE") map println
    Future.successful(Ok(JsNumber(50)))
  }

  def security(lat: Long, lng: Long) = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }

  def digital(lat: Long, lng: Long) = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }

  def health(lat: Long, lng: Long) = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }
  
  def weather(lat: Long, lng: Long) = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }



}


