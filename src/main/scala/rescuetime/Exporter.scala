package rescuetime


import scala.util.{Success, Failure}
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.event.Logging
import akka.io.IO
import spray.can.Http
import spray.httpx.SprayJsonSupport
import spray.client.pipelining._
import spray.util._


/**
 * Main application. As of now, uses the first arg as the API key and fetches the hardcoded range.
 *
 * No persistence of data.. yet
 */
object Exporter extends App {

  val key = args(0)
  val perspective = "interval"
  val beginDate = "2014-08-28"
  val endDate = "2014-08-29"

  // spin up an actor system
  implicit val system = ActorSystem("simple-spray-client")
  import system.dispatcher
  val log = Logging(system, getClass)

  log.info("Requesting RescueTime data")
  import JsonProtocol._
  import SprayJsonSupport._

  val pipeline = sendReceive ~> unmarshal[ApiResult]
  val responseFuture = pipeline {
    Get(URLBuilder.url(key, beginDate, endDate))
  }

  responseFuture onComplete {
    case Success(apiResult @ ApiResult(notes, row_headers, rows)) =>

      val result: Result = apiResult
      val categoryVsEntry = result.entries.groupBy(t => t.category)

      // print all fancy-like
      categoryVsEntry.foreach(
        p => {
          println("\n" + p._1)
          p._2.foreach(
            timeEntry =>
              println("\t" + timeEntry)
          )
        })

      shutdown()
    case Success(somethingUnexpected) =>
      log.warning("The RescueTime API call was successful but returned something unexpected: '{}'.", somethingUnexpected)
      shutdown()
    case Failure(error) =>
      log.error(error, "Couldn't get data")
      shutdown()
  }
  def shutdown(): Unit = {
    IO(Http).ask(Http.CloseAll)(1.second).await
    system.shutdown()
  }
}
