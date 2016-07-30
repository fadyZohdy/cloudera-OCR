package services

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
/**
  * Created by droidman on 7/29/16.
  */
object ElasticDAO {
  val client  = ElasticClient.remote("172.17.0.4", 9300)
}
