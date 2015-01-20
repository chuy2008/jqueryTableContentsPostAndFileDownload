package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._

import java.io.File

import models._
import views._


object Application extends Controller
{
  
  lazy val lOfBusUnits = AppBusinessUnitIdDB.findAll
  val mainPathForFiles = new java.io.File("test.txt").getAbsolutePath().substring(0,
                               new java.io.File("test.txt").getAbsolutePath().length() - "test.txt".length())
  type mappedBusinessStatRecord = Map[String, String]
  type mappedBusinessStatRecordList = List[mappedBusinessStatRecord]
  

  def loop(statsListRemaining: List[BusinessUnitStats], 
           acum: mappedBusinessStatRecordList): mappedBusinessStatRecordList =
  {
     if (!statsListRemaining.headOption.isEmpty)
     {
        val busUnitName = AppBusinessUnitIdDB.findByKey(statsListRemaining.head.busUnitKey) match
        {
           case Some(x) => x.name
           case None    => "unknown"
        }
        loop(statsListRemaining.tail, 
           List(("name", busUnitName),
                ("quarter", statsListRemaining.head.quarter.toString),
                ("sales", statsListRemaining.head.quarterSales.toString),
                ("projectsClosed", statsListRemaining.head.projectsClosed.toString),
                ("newProjects", statsListRemaining.head.newProjects.toString),
                ("manHours", statsListRemaining.head.manHoursSalesTasks.toString)).toMap :: acum)      
     }
     else {acum}
  }																	// def loop .....  
  
  def index = Action 
  {
     request =>
       request.session.get("fileCleanUpNeeded") match
       {
         case Some(x) => x match
           {
              case "yes" => {FileExplorer.cleanUpFilesWhoseNameIncludes(
                                  request.session.get("fileId"), mainPathForFiles)}
              case _     => {}    
           }
         case None    => {}
       } 
       Ok(views.html.index("Please select one option")("")(lOfBusUnits)).withSession(
           "fileId" -> RandomCode.generateRandomCode(5), "fileCleanUpNeeded" -> "no")
  }
  
  def displayStatsForBusinessUnitSelected(busUnit: BusinessUnitId) = Action
  {
     request =>
       request.session.get("fileCleanUpNeeded") match
       {
         case Some(x) => x match
           {
              case "yes" => {FileExplorer.cleanUpFilesWhoseNameIncludes(
                                   request.session.get("fileId"), mainPathForFiles)}
              case _     => {}    
           }
         case None    => {}
       } 
       Ok(views.html.businessUnitStatsTable("Stats for a Single Business Unit")(busUnit.name)(lOfBusUnits)
         (loop(AppBusinessUnitStatsDB.findStatsFromBusUnit(busUnit), List()))).withSession(
           "fileId" -> request.session.get("fileId").getOrElse("11"), "fileCleanUpNeeded" -> "no")
  }
  
  def displayStatsForAllBusinessUnits = Action
  {
     request =>
       request.session.get("fileCleanUpNeeded") match
       {
         case Some(x) => x match
           {
              case "yes" => {FileExplorer.cleanUpFilesWhoseNameIncludes(
                                 request.session.get("fileId"), mainPathForFiles)}
              case _     => {}    
           }
         case None    => {}
       }     
    Ok(views.html.businessUnitStatsTable("Stats for all Business Units")("allBusinessUnits")(lOfBusUnits)
         (loop(AppBusinessUnitStatsDB.findAll, List()))).withSession(
           "fileId" -> request.session.get("fileId").getOrElse("11"), "fileCleanUpNeeded" -> "no")    
  }
  
  def processTableJsonDataForExcelConversion(fileName: String) = Action(parse.json)
  {
     request =>
       request.session.get("fileCleanUpNeeded") match
       {
         case Some(x) => x match
           {
              case "yes" => {FileExplorer.cleanUpFilesWhoseNameIncludes(
                                  request.session.get("fileId"), mainPathForFiles)}
              case _     => {}    
           }
         case None    => {}
       }           
       val salesDataInTable = request.body.as[List[Map[String, String]]]
       val fileNameAndPath  = ExcelConverter.excelConv(fileName + "-" + request.session.get("fileId").getOrElse("noFileIdInSesion"), 
                                                         mainPathForFiles, 
                                                         salesDataInTable)
        Ok(views.html.showDownloadFileLink(fileNameAndPath)).withSession(
            "fileId" -> request.session.get("fileId").getOrElse("11"), "fileCleanUpNeeded" -> "yes")
                
  }
   
  def downloadFile(fileNameAndPath: String) = Action
  {
    request =>
      Ok.sendFile(
        content = new java.io.File(fileNameAndPath),
        fileName = _ => "SalesStatsForYourAnalysis.xlsx").withSession(
           "fileId" -> request.session.get("fileId").getOrElse("11"), "fileCleanUpNeeded" -> "yes")                   
  }
  
}


