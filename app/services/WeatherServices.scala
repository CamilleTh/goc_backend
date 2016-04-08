package services
import play.api._
import play.api.mvc._
import play.api.Play.current
import javax.inject._

import play.api.Play.current
//import play.api.libs.ws._
//import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


import play.api.libs.json.Json
//
//case class OpenWeather(id: Long, main: String, description: String, icon: String)
//
class WeatherServices @Inject(){
//(ws: WSClient) {
//  
//  implicit val myJsonFormat = Json.format[OpenWeather]
//  
//  def get = {
//		val id:Long		= 2657896
//		val cnt:Int		= 2 // # of forecasted days 
//		val url:String	= "http://api.openweathermap.org/data/2.5/forecast/daily?id="+id+"&cnt="+cnt+"&mode=json"
//		//val url:String = "http://api.openweathermap.org/data/2.5/weather?id="+id
//
//		val tbParser:String = """{"code": "salut", "array": [{"b": "dsa"},{"b": "fds"},{"b": "rewq"}]}"""
//
//		println((Json.parse(tbParser) \ "array" \\ "b").map{_.as[String]})
//
//		val holder: WSRequestHolder = ws.url(url)
//		val futureResponse: Future[WSResponse] = holder.get()
//
//		futureResponse.map{a=> 	
//			(a.json \ "list"  \\ "weather").map{b => 
//				(b \\ "icon").map{_.as[String]}
//			}
//
//			(a.json \ "list" \\ "weather").map{_.as[OpenWeather]}
//		}
//	}
  
}