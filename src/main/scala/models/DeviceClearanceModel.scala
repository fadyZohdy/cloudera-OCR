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
    object submitter_info extends StringColumn(this)
    object case_num extends StringColumn(this)
    object regNum extends StringColumn(this) with PartitionKey[String]
    object regName extends StringColumn(this)
    object regClass extends StringColumn(this)
    object productCode extends StringColumn(this)
    object dated extends StringColumn(this)
    object received extends StringColumn(this)
    object deviceName extends StringColumn(this)
    object approved extends StringColumn(this)

    override def fromRow(row: Row): entities.DeviceClearnace = {
      DeviceClearnace(
        fileName(row),
        submitter_info(row),
        case_num(row),
        regNum(row),
        regName(row),
        regClass(row),
        productCode(row),
        dated(row),
        received(row),
        deviceName(row),
        approved(row)
      )
    }
  }

  abstract class ConcreteDeviceClearanceTable extends DeviceClearnace with RootConnector{

    override lazy val tableName = "device_clearance"

    def store(clearance: entities.DeviceClearnace): Future[ResultSet] = {
      insert.value(_.fileName, clearance.fileName)
        .value(_.submitter_info, clearance.submitter_info)
        .value(_.case_num, clearance.case_num)
        .value(_.regNum, clearance.regNum)
        .value(_.regName, clearance.regName)
        .value(_.regClass, clearance.regClass)
        .value(_.productCode, clearance.productCode)
        .value(_.dated, clearance.dated)
        .value(_.received, clearance.received)
        .value(_.deviceName, clearance.deviceName)
        .value(_.approved, clearance.approved)
        .consistencyLevel_=(ConsistencyLevel.ONE)
        .future()
    }

    def getByRegNum(regNum: String): Future[Option[entities.DeviceClearnace]] = {
      select.where(_.regNum eqs regNum).one
    }
  }
}
