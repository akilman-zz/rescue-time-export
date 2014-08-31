package rescuetime

object TestContants {
  val notes = "some notes"
  val headers = List("rank", "nSeconds", "nPeople", "activity", "category", "productivity")
  val timeEntry = TimeEntry(1, 1, 1, "activity", "category", 1)
  val queryResult = QueryResult(notes, headers, List(timeEntry))
}
