package com.demo

import akka.actor.ActorSystem
import com.demo.model.{PremiumUser, User}
import spray.http.MediaType
import spray.routing.{Route, SimpleRoutingApp}

object PostUser extends App with SimpleRoutingApp{

  implicit val actorSystem = ActorSystem()

  var users = User.users

  def getJson(route: Route) = get {
    respondWithMediaType(MediaType.custom("application/json")) {
      route
    }
  }

  startServer(interface="localhost", port=8000){
    get{
      path("hello"){ ctx =>
        ctx.complete("First Akka Hello Version")
      }
    } ~
    getJson{
      path("list" / "all"){
        complete{
          User.toJson(users)
        }
      }
    } ~
    get{
      path("user" / IntNumber / "details"){ index =>
        complete{
          User.toJson(users(index))
        }
      }
    } ~
    post{
      path("user" / "add" / "premium"){
        parameters("name", "balance".as[Int]) { (name, balance) =>
          val newUser = PremiumUser(name,balance)
          users = newUser :: users
          complete{
            "OK"
          }
        }
      }
    }
  }
}
