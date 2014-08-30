package rescuetime

import org.scalatest.{Matchers, FlatSpec}

class JsonProtocolSpec extends FlatSpec with Matchers {

  "A JsonProtocol " should " map rescuetime.Result instances to JSON and back" in {

    // for implicit conversions
    import JsonProtocol._

    val notes = "some notes"
    val headers = List("rank", "nSeconds", "nPeople", "activity", "category", "productivity")
    val timeEntry = TimeEntry(1, 1, 1, "activity", "category", 1)

    // start with a proper result
    val result = Result(notes, headers, List(timeEntry))

    // convert to an API result that spray-json can consume via implicit conversion
    val apiResult:ApiResult = result

    // check intermediate form
    val eitherList: List[Either[Int, String]] = timeEntry
    val apiResultExpected = ApiResult(notes, headers, List(eitherList))
    apiResult should equal(apiResultExpected)

    // convert to json
    import spray.json._
    val json = apiResult.toJson.prettyPrint

    // convert back to intermediate form
    val jsonAst = json.parseJson
    val parsedApiResult = jsonAst.convertTo[ApiResult]
    parsedApiResult should equal(apiResult)

    // and once again, back to first class form
    val parsedResult:Result = parsedApiResult
    parsedResult should equal(result)
  }
}
