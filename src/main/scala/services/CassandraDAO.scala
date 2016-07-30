package services

import com.websudos.phantom.connectors.ContactPoints
import com.websudos.phantom.dsl._
import models.DeviceClearanceModel.ConcreteDeviceClearanceTable

import scala.concurrent.Await
import scala.concurrent.duration._
/**
  * Created by droidman on 7/29/16.
  */


  object Defaults {
    val connector = ContactPoints(Seq("172.17.0.2")).keySpace("medstreaming")
  }

  class ClearanceDatabase(override val connector: KeySpaceDef) extends Database(connector) {
    // And here you inject the real session and keyspace in the table
    object Clearance extends ConcreteDeviceClearanceTable with connector.Connector
  }

  object CassandraDAO extends ClearanceDatabase(Defaults.connector)

  object DB extends Defaults.connector.Connector{

    def createTables(): Unit = {
      Await.result(CassandraDAO.autocreate.future(), 5000 millis)
    }

  }

