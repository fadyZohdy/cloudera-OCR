package services

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{RichSearchHit, HitAs}
import play.api.libs.json.Json

/**
  * Created by droidman on 7/30/16.
  */
object WebService extends App{

  case class ElasticResult(regNum: String, deviceName: String)

  implicit object ElasticResultHitAs extends HitAs[ElasticResult] {
    override def as(hit: RichSearchHit): ElasticResult = {
      ElasticResult(hit.sourceAsMap("regNum").toString, hit.sourceAsMap("deviceName").toString)
    }
  }

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val route =
    path("/"){
      post{
        parameter("query"){q =>
          val result = ElasticDAO.client.execute{
            search in "medstreaming" / "clearances" query q limit 1
          }.await
          val clearances :Seq[ElasticResult] = result.as[ElasticResult]

          val f = CassandraDAO.Clearance.getByRegNum(clearances(0).regNum)

          onSuccess(f) {
            case Some(item) => complete(Json.toJson(item).toString)
            case None       => complete(StatusCodes.NotFound)
          }
        }
      }
    }

  val serverBinding = Http().bindAndHandle(route, "localhost", 8888)

}
