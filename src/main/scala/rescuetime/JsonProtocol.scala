package rescuetime

import spray.json.DefaultJsonProtocol

/**
 * JSON output is of the form:
 *
 * {{{
 *   {
    "notes": "data is an array of arrays (rows), column names for rows in row_headers",
    "row_headers": [
        "Rank",
        "Time Spent (seconds)",
        "Number of People",
        "Activity",
        "Category",
        "Productivity"
    ],
    "rows": [
        [
            1,
            16611,
            1,
            "iTerm",
            "Systems Operations",
            2
        ],
  ]
    }
 * }}}
 *
 * The type of the `rows` field is a bit weird, but this is a consequence of the array returned not having proper
 * labels.
 *
 * TODO - There's probably a cleaner way to represent this...
 *
 * @param notes useless field
 * @param row_headers headers for each segment for each `row` entry
 * @param rows time entries
 */
case class ApiResult(notes: String, row_headers: List[String], rows: List[List[Either[Int, String]]])

/**
 * Creating a first class type for each entry so its more clear to the user
 * @param rank rank of activity
 * @param nSeconds seconds activity lasted
 * @param nPeople number of people who participated
 * @param activity string representing activity
 * @param category category of said activity
 * @param productivity level of productivity
 */
case class TimeEntry(rank: Int, nSeconds: Int, nPeople: Int, activity: String, category: String, productivity: Int)
case class Result(notes: String, rowHeaders: List[String], entries: List[TimeEntry])

/**
 * RescueTime API JSON protocol
 *
 * CONVERT ALL THE THINGS!!
 */
object JsonProtocol extends DefaultJsonProtocol {

  /**
   * Implicit JsonWriter to translate between JSON <=> ApiResult
   */
  implicit val rescueTimeApiResultFormat  = jsonFormat3(ApiResult)

  def eitherListToTimeEntry(row: List[Either[Int, String]]): TimeEntry = {
    row match {
      case List(Left(rank), Left(nSeconds), Left(nPeople), Right(activity), Right(category), Left(productivity)) =>
        TimeEntry(rank, nSeconds, nPeople, activity, category, productivity)
    }
  }
  
  implicit def timeEntryToEitherList(e: TimeEntry): List[Either[Int, String]] = {
    List(Left(e.rank), Left(e.nSeconds), Left(e.nPeople), Right(e.activity), Right(e.category), Left(e.productivity))
  }

  implicit def convertApiResultToProperResult(apiResult: ApiResult):Result = {
    val entries = apiResult.rows.map(eitherListToTimeEntry)
    Result(apiResult.notes, apiResult.row_headers, entries)
  }

  implicit def convertResultToApiResult(result: Result): ApiResult = {
    val rows = result.entries.map(timeEntryToEitherList)
    ApiResult(result.notes, result.rowHeaders, rows)
  }
}
