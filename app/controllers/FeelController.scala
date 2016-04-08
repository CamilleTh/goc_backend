package controllers

import javax.inject._

import models.City
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import reactivemongo.api.ReadPreference
import reactivemongo.play.json._
import reactivemongo.play.json.collection._
import utils.Errors

import scala.concurrent.{ExecutionContext, Future}


/**
 * Simple controller that directly stores and retrieves [models.City] instances into a MongoDB Collection
 * Input is first converted into a city and then the city is converted to JsObject to be stored in MongoDB
 */
@Singleton
class FeelController @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents {


  def transport = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }

  def security = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }

  def digital = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }

  def health = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }
  
  def weather(lat: Long, long: Long) = Action.async{
    Future.successful(Ok(JsNumber(50)))
  }

}


