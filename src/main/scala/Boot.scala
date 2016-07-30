
import akka.actor.ActorSystem
import com.sksamuel.elastic4s.{HitAs, RichSearchHit}
import org.apache.spark.{SparkConf, SparkContext}
import services.{ElasticDAO, CassandraDAO, DB, OCRExtractor}
import com.sksamuel.elastic4s.ElasticDsl._
import akka.actor.Status. {Success, Failure }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout

/**
  * Created by droidman on 7/28/16.
  */
object Boot extends App{

  DB.createTables

  val conf = new SparkConf().setAppName("demo").setMaster("local[*]")
  val sc = new SparkContext(conf)

  val files = sc.binaryFiles("/run/media/droidman/Data/medstreamig_demo")

  //convert function takes a pdf file and returns a DeviceClearance object to be inserted in cassandra and elastic
  files.map(OCRExtractor.convertFunc(_)).foreach{ clearance =>

    CassandraDAO.Clearance.store(clearance)

      ElasticDAO.client.execute {
        index into "medstreaming" / "clearances" id clearance.regNum fields (
          "regNum" -> clearance.regNum,
          "deviceName" -> clearance.deviceName
          )
      }.await
  }


}
