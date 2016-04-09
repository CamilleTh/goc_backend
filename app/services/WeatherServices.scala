package services
import javax.inject._
import play.api.libs.ws._
import utils.{ Constants }
import models.OpenWeather._
import models.OpenWeatherResponse
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{ JsValue, JsObject, JsArray }

@Singleton
class WeatherServices @Inject() (constants: Constants, ws: WSClient) {

  def getWeatherFeelingFromOpenWeather(lat: Double, lng: Double) = {

    val url = constants.OPEN_WEATHER_API_URL;
    val urlWithParam = ws.url(url).withQueryString("appid" -> constants.OPEN_WEATHER_API_KEY, "lat" -> lat.toString(), "lon" -> lng.toString())
    println(urlWithParam.uri.toString());
    urlWithParam.get map {
      response =>
        response.json.asOpt[OpenWeatherResponse] map {
          _.weather.head.id match {
            case additional if (additional > 950)     => println(50); 50;
            case extreme if (extreme > 900)           => println(1); 1;
            case cloud if (cloud > 801)               => println(80); 80;
            case clear if (clear > 800)               => println(95); 95;
            case atmosphere if (atmosphere > 700)     => println(5); 5;
            case snow if (snow > 600)                 => println(5); 5;
            case rain if (rain > 500)                 => println(25); 25
            case drizzle if (drizzle > 300)           => println(20); 20;
            case thunderstorm if (thunderstorm > 200) => println(10); 10;
            case other => {
              println("WARNING, BAD RETURN OpenWeather:" + other);
              0;
            }
          }
        }
    }
  }

}