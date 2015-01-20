package models

import play.api.mvc.PathBindable
import scala.collection.immutable.Map
import play.api.db._
import org.squeryl._
import org.squeryl.PrimitiveTypeMode._

import controllers._

object BusinessUnitId
{
   implicit def pathBinder(implicit strBinder: PathBindable[String]) = new PathBindable[BusinessUnitId] 
    {
       override def bind(key: String, value: String): Either[String, BusinessUnitId] =
        {           
           AppBusinessUnitIdDB.findByKey(value.toInt) match
           {
              case Some(x) => Right(new BusinessUnitId(x.name, x.keyy))
              case None    => Left("for some strange reason, Business Unit is not in database, " +
                                    "or at least the following key = " + value + "does not appear " +
                   		            "to exist == BusinessUnitId scala routine, within pathBinder function")
           }
        }        
        override def unbind(key: String, busUnitId: BusinessUnitId): String = 
        {
            strBinder.unbind(key, busUnitId.keyy.toString)
        }
     }  
}

case class BusinessUnitId(name: String, keyy: Int) extends KeyedEntity[Long]
{
     val id: Long = 0
}

object AppBusinessUnitIdDB extends Schema
{
    val businessUnitIdTable = table[BusinessUnitId]("businessUnitId")

    def create(busUnitId: BusinessUnitId) = 
    {
       inTransaction
       {
          AppBusinessUnitIdDB.businessUnitIdTable insert busUnitId
       }
    }
    
    def findAll: List[BusinessUnitId] =
    {
       inTransaction
       {
          from(AppBusinessUnitIdDB.businessUnitIdTable)(s => select(s)).toSeq.toList
       }   
    }

   def findByKey(busUnitKey: Int): Option[BusinessUnitId] = 
   {
      inTransaction
      {
        from(AppBusinessUnitIdDB.businessUnitIdTable)(
                s => where(s.keyy === busUnitKey) select(s)).toList.headOption
      }
   }    
    
}