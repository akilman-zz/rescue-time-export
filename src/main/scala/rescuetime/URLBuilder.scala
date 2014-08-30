package rescuetime

/**
 * Simple helper for constructing URLs with given parameters via String interpolation
 */
object URLBuilder {

  def url(key: String, beginDate: String, endDate: String) =
    s"https://www.rescuetime.com/anapi/data?" +
            s"key=${key}&" +
            s"format=json&" +
            s"restrict_begin=${beginDate}&" +
            s"restrict_end=${endDate}"
}
