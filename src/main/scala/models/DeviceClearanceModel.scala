package models

import com.datastax.driver.core.ConsistencyLevel
import com.websudos.phantom.dsl._
import entities.DeviceClearnace

import scala.concurrent.Future

/**
  * Created by droidman on 7/29/16.
  */
object DeviceClearanceModel {

  class DeviceClearnace extends CassandraTable[ConcreteDeviceClearanceTable, entities.DeviceClearnace] {

    object fileName extends StringColumn(this)
    object regNum extends StringColumn(this) with PartitionKey[String]
    object regName extends StringColumn(this)
    object regClass extends StringColumn(this)
    object productCode extends StringColumn(this)
    object dated extends StringColumn(this)
    object received extends StringColumn(this)
    object deviceName extends StringColumn(this)

    override def fromRow(row: Row): entities.DeviceClearnace = {
      DeviceClearnace(
        fileName(row),
        regNum(row),
        regName(row),
        regClass(row),
        productCode(row),
        dated(row),
        received(row),
        deviceName(row)
      )
    }
  }

  abstract class ConcreteDeviceClearanceTable extends DeviceClearnace with RootConnector{

    override lazy val tableName = "device_clearance"

    def store(clearance: entities.DeviceClearnace): Future[ResultSet] = {
      insert.value(_.fileName, clearance.fileName)
        .value(_.regNum, clearance.regNum)
        .value(_.regName, clearance.regName)
        .value(_.regClass, clearance.regClass)
        .value(_.productCode, clearance.productCode)
        .value(_.dated, clearance.dated)
        .value(_.received, clearance.received)
        .value(_.deviceName, clearance.deviceName)
        .consistencyLevel_=(ConsistencyLevel.ONE)
        .future()
    }

    def getByRegNum(regNum: String): Future[Option[entities.DeviceClearnace]] = {
      select.where(_.regNum eqs regNum).one
    }
  }
}
