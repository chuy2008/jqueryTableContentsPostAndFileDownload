import org.squeryl.adapters.{H2Adapter, PostgreSqlAdapter, MySQLAdapter}
import org.squeryl.internals.DatabaseAdapter
import org.squeryl.{Session, SessionFactory}
import play.api.db.DB
import play.api.GlobalSettings
import scala.io.Codec.UTF8

import play.api.mvc.{RequestHeader, Handler}

import play.api.Application
import models._
import controllers._

object Global extends GlobalSettings {

   def getSession(adapter:DatabaseAdapter, app: Application) = Session.create(DB.getConnection()(app), adapter)
  
   override def onStart(app: Application) 
   {    
       SessionFactory.concreteFactory = app.configuration.getString("db.default.driver") match 
       {
          case Some("org.h2.Driver") => Some(() => getSession(new H2Adapter, app))
          case Some("org.postgresql.Driver") => Some(() => getSession(new PostgreSqlAdapter, app))
          case Some("com.mysql.jdbc.Driver") => Some(() => getSession(new MySQLAdapter, app) )
          case _ => sys.error("Database driver must be either org.h2.Driver or org.postgresql.Driver")
       } 
       InitialData.insert()
   }
}

object InitialData
{
    def insert() =
    {
        
       if(AppBusinessUnitIdDB.findAll.isEmpty)
       {
          Seq(
             BusinessUnitId("US", 1),
             BusinessUnitId("USServiceProviders", 2),
             BusinessUnitId("USEnterprise", 3),
             BusinessUnitId("USSmallMedium", 4),
             BusinessUnitId("AsiaPacific", 5),
             BusinessUnitId("China", 6),
             BusinessUnitId("Japan", 7),
             BusinessUnitId("Europe", 8),             
             BusinessUnitId("Africa", 9),
             BusinessUnitId("Canada", 10),
             BusinessUnitId("LatinAmerica", 11)             
             ).foreach(AppBusinessUnitIdDB.create)
       }
    }
    
       if(AppBusinessUnitStatsDB.findAll.isEmpty)
       {
          Seq(
             BusinessUnitStats(1, 1, 900000, 5, 3, 120),
             BusinessUnitStats(2, 1, 500000, 2, 2, 110),
             BusinessUnitStats(3, 1, 300000, 3, 1, 100),
             BusinessUnitStats(4, 1, 980000, 4, 3, 90),
             BusinessUnitStats(5, 1, 500000, 8, 7, 130),
             BusinessUnitStats(6, 1, 300000, 2, 9, 100),
             BusinessUnitStats(7, 1, 300000, 1, 2, 80),
             BusinessUnitStats(8, 1, 920000, 6, 7, 70),
             BusinessUnitStats(9, 1, 500000, 4, 9, 95),
             BusinessUnitStats(10, 1, 350000, 3, 5, 110),             
             BusinessUnitStats(11, 1, 330000, 2, 8, 100),
             BusinessUnitStats(1, 2, 900000, 2, 3, 100),
             BusinessUnitStats(2, 2, 510000, 6, 2, 90),
             BusinessUnitStats(3, 2, 280000, 1, 1, 75),
             BusinessUnitStats(4, 2, 950000, 2, 3, 90),
             BusinessUnitStats(5, 2, 490000, 8, 2, 95),
             BusinessUnitStats(6, 2, 320000, 3, 1, 105),
             BusinessUnitStats(7, 2, 340000, 6, 1, 104),
             BusinessUnitStats(8, 2, 900000, 7, 3, 110),
             BusinessUnitStats(9, 2, 580000, 3, 2, 98),
             BusinessUnitStats(10, 2, 310000, 2, 1, 100),             
             BusinessUnitStats(11, 2, 350000, 7, 1, 103)                     
             ).foreach(AppBusinessUnitStatsDB.create)
       }    
}

