package entities

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
  * Created by droidman on 7/29/16.
  */
case class DeviceClearnace(fileName: String,
                           regNum: String,
                           regName: String,
                           regClass: String,
                           productCode: String,
                           dated: String,
                           received: String,
                           deviceName: String)

object DeviceClearnace{
  def apply(fileName: String, lines: String): DeviceClearnace = {
    val regNumRegex = """Regulation Number:\s+(.+)""".r
    val regNameRegex = """Regulation Name:\s+(.+)""".r
    val regClassRegex = """Regulatory Class:\s+(.+)""".r
    val productCodeRegex = """Product Code:\s+(.+)""".r
    val datedRegex = """Dated:\s+(\w{3,10}\s+\d{1,2},\s+\d{4}).*""".r
    val receivedRegex = """Received:\s+(\w{3,10}\s+\d{1,2},\s+\d{4}).*""".r
    val deviceNameRegex = """Trade/Device Name:\s+(.+)""".r

    val m = scala.collection.mutable.Map[String, String]()

    lines.split("\n").foreach { _ match {
      case regNumRegex( regNum ) => m += ("regNum" -> regNum)
      case regNameRegex(regName) => m += ("regName" -> regName)
      case regClassRegex( regClass ) => m += ("regClass" -> regClass)
      case productCodeRegex( productCode ) => m += ("productCode" -> productCode)
      case datedRegex( dated ) => m += ("dated" -> dated)
      case receivedRegex( received ) => m += ("received" -> received)
      case deviceNameRegex( deviceName ) => m += ("deviceName" -> deviceName)
      case _ => print( "" )
      }
    }
    DeviceClearnace(fileName, m("regNum"), m("regName"), m("regClass"), m("productCode"), m("dated"), m("received"), m("deviceName"))
  }

  implicit val clearanceWrites = new Writes[DeviceClearnace] {
    def writes(clearance: DeviceClearnace) = Json.obj(
      "regNum" -> clearance.regNum,
      "regName" -> clearance.regName,
      "regClass" -> clearance.regClass,
      "productCode" -> clearance.productCode,
      "dated" -> clearance.dated,
      "received" -> clearance.received,
      "deviceName" -> clearance.deviceName)
  }

}
