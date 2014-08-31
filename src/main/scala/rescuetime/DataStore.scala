package rescuetime

import java.io.{File, PrintWriter}
import org.slf4j.LoggerFactory
import scala.io.Source
import spray.json._
import JsonProtocol._

object DataStore {

  private val log = LoggerFactory.getLogger("DataStore")

  def writeToDisk(dataSet: DataSet, directory: File): String = {

    val filename = s"rescue-time_${dataSet.startDate}_to_${dataSet.endDate}.json"
    val path = directory.getAbsolutePath + File.separator + filename
    val printWriter = new PrintWriter(path)

    log.info(s"Writing data set to disk: ${path}")

    printWriter.write(dataSet.toJson.prettyPrint)
    printWriter.close()

    path
  }

  def readFromDisk(path: String): DataSet = {
    readFromDisk(new File(path))
  }

  def readFromDisk(file: File): DataSet = {

    log.info(s"Reading data set form disk: ${file.getAbsolutePath}")

    val contents = Source.fromFile(file).getLines().mkString
    val jsonAst = contents.parseJson
    jsonAst.convertTo[DataSet]

  }
}


