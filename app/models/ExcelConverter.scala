package models

import java.io.File
import java.io.FileOutputStream
import org.apache.poi.xssf.usermodel._

import controllers._
import views._

object ExcelConverter 
{

   def excelConv(fileeName: String, pathh: String, statsOnTable: List[Map[String, String]]): String =
   {
      val wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook
      val sheet = wb.createSheet("Sheet1")
      val fileName = pathh  + "/" + fileeName + "-" + "excelFile.xlsx"
      val excelFileForDownload = new File(fileName)
      val excelFileForDownloadOutStream = new FileOutputStream(excelFileForDownload);

      var row  = sheet.createRow(0)
      var cell = row.createCell(0)     
      
      def loop(remStats: List[Map[String, String]], presentRow: Int): Boolean =
      {
         if (!remStats.headOption.isEmpty)
         {
            if (remStats.head("rowLoc").toInt != presentRow)
            {
               row  = sheet.createRow(remStats.head("rowLoc").toInt)
               cell = row.createCell(remStats.head("colLoc").toInt)   
               cell.setCellValue(remStats.head("val"))   
               loop(remStats.tail, presentRow + 1)
            }
            else
            {
               cell = row.createCell(remStats.head("colLoc").toInt)   
               cell.setCellValue(remStats.head("val")) 
               loop(remStats.tail, presentRow)
            }
         }
         else{true}
      }
      val finished = loop(statsOnTable, 0)
      wb.write(excelFileForDownloadOutStream)
      excelFileForDownloadOutStream.flush
      excelFileForDownloadOutStream.close
      fileName
   }

  
}