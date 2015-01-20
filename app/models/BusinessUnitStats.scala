package models

import scala.collection.immutable.Map
import play.api.db._
import org.squeryl._
import org.squeryl.PrimitiveTypeMode._

import controllers._

case class BusinessUnitStats(busUnitKey: Int, quarter: Int, quarterSales: Int, 
                             projectsClosed: Int, newProjects: Int,
                             manHoursSalesTasks: Int) extends KeyedEntity[Long]
{
     val id: Long = 0
}

object AppBusinessUnitStatsDB extends Schema
{
    val businessUnitStatsTable = table[BusinessUnitStats]("businessUnitStats")

    def create(busUnitStats: BusinessUnitStats) = 
    {
       inTransaction
       {
          AppBusinessUnitStatsDB.businessUnitStatsTable insert busUnitStats
       }
    }
    
    def findAll: List[BusinessUnitStats] =
    {
       inTransaction
       {
          from(AppBusinessUnitStatsDB.businessUnitStatsTable)(s => select(s)).toSeq.toList
       }   
    }
    
    def findStatsFromBusUnit(busUnit: BusinessUnitId): List[BusinessUnitStats] =
    {
       inTransaction
       {
          from(AppBusinessUnitStatsDB.businessUnitStatsTable)(
               s => where(s.busUnitKey === busUnit.keyy) select(s)).toSeq.toList
       }
    }   
}