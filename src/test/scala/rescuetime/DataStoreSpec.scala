package rescuetime

import org.scalatest.{Matchers, FlatSpec}
import com.google.common.io.Files

class DataStoreSpec extends FlatSpec with Matchers {

  "A DataStore " should " write a DataSet to disk " in {

    import TestContants._
    import DataStore._

    val dataset = DataSet("2014-01-01", "2014-08-29", List(queryResult))
    val directory = Files.createTempDir()

    writeToDisk(dataset, directory)
  }

  "A DataStore " should " read a DataSet from disk " in {
    import TestContants._
    import DataStore._

    val dataset = DataSet("2014-01-01", "2014-08-29", List(queryResult))
    val directory = Files.createTempDir()

    val path = writeToDisk(dataset, directory)
    val readDataset = readFromDisk(path)

    dataset should equal(readDataset)
  }

}
