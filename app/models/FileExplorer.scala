package models

import java.io._

object FileExplorer 
{
  
  def cleanUpFilesWhoseNameIncludes(strOption: Option[String], presentDirectory: String) =
  {
    strOption match
    {
      case Some(x) => 
        {
           def loop(filesRemaining: List[java.io.File], sessionId: String): Boolean =
           {
              if (!filesRemaining.headOption.isEmpty)
              {
                 filesRemaining.head.isFile match
                 {
                   case true  => 
                     {
                       filesRemaining.head.getName() contains sessionId match
                       {
                         case true => 
                           {
                              filesRemaining.head.delete()
                              loop(filesRemaining.tail, sessionId)
                           }
                         case _ => {loop(filesRemaining.tail, sessionId)}
                       }
                     }
                   case false => loop(filesRemaining.tail, sessionId) 
                 }
              }
              else {true}
           }
           loop(new java.io.File(presentDirectory).listFiles().toList, x)
        }
      case None    => {}
    }
    
  }

}