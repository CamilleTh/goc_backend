package services
import javax.inject._
import play.modules.reactivemongo.{ ReactiveMongoComponents, MongoController, ReactiveMongoApi }
import scala.concurrent.{ Future, ExecutionContext }
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json.{ JsObject, JsNumber, JsArray, Json }
import utils.Constants;
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import reactivemongo.api.DefaultDB
import models.DigitalInternet._
import models.DigitalInternetResponse

@Singleton
class DigitalService @Inject() (val reactiveMongoApi: ReactiveMongoApi, val geoCodageService: GeoCodageService, constant: Constants)(implicit exec: ExecutionContext) {

  def static_data: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("static-data"))

  def getUHD3(lat: Double, lng: Double): Future[Option[DigitalInternetResponse]] = {
    val commune = geoCodageService.getCityFromCoordinates(lat, lng);
    commune.flatMap { optionCommune =>
      optionCommune match {
        case Some(x) =>
          println("CommuneGoogle : " + x);
          for {
            uHD3 <- static_data
            couverture <- uHD3.find(
              Json.obj(
                "type" -> constant.DATA_DIGITAL,
                "subtype" -> constant.DATA_COUVERTURE,
                "city" -> x)).cursor[DigitalInternetResponse]().headOption
          } yield {
            println("SomeUHD3 : " + couverture)
            couverture
          }
        case None => println("NoneOptionCommune"); Future(None);
      }
    }
  }
}