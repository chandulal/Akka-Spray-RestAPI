package com.demo

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.demo.model.{PremiumUser, User}
import spray.http.MediaType
import spray.routing._

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object HelloActor extends App with SimpleRoutingApp {

  implicit val actorSystem = ActorSystem()

  var users = User.users
  implicit val timeout = Timeout(1.second)

  lazy val helloActor = actorSystem.actorOf(Props(new HelloActor()))
  lazy val getBalanceActor = actorSystem.actorOf(Props(new GetBalanceActor()))

  def getJson(route: Route) = get {
    respondWithMediaType(MediaType.custom("application/json")) {
      route
    }
  }

  lazy val userRoute = {
    get {
      path("hello") { ctx =>
        helloActor ! ctx
      }
    } ~
      getJson {
        path("list" / "all") {
          complete {
            User.toJson(users)
          }
        }
      } ~
      get {
        path("user" / IntNumber / "details") { index =>
          complete {
            User.toJson(users(index))
          }
        }
      } ~
      post {
        path("user" / "add" / "premium") {
          parameters("name", "balance".as[Int]) { (name, balance) =>
            val newUser = PremiumUser(name, balance)
            users = newUser :: users
            complete {
              "OK"
            }
          }
        }
      }
  }
  lazy val balanceRoute = {
    get {
      path("premiumUser" / "balance") {
        parameter("name") { name =>
          println(name)
          complete {
             (getBalanceActor ? name).mapTo[Int]
               .map(bal => s"your balance is $bal")
          }
        }
      }
    }
  }
  startServer(interface = "localhost", port = 8000) {
    userRoute ~ balanceRoute
  }

  class HelloActor extends Actor {
    override def receive = {
      case ctx: RequestContext => ctx.complete("First Akka Hello Version")
    }
  }

  class GetBalanceActor extends Actor {
    override def receive = {
      case name: String => {
        val user = User.getByName(name)
        val balance = user match {
          case Some(PremiumUser(name, balance)) => balance
          case _ => 0
        }
        sender ! balance
      }
    }
  }
}
