package com.demo

import akka.actor.ActorSystem
import com.demo.model.User
import spray.http.MediaType
import spray.routing.SimpleRoutingApp

object HelloAkka extends App with SimpleRoutingApp {

  implicit val actorSystem = ActorSystem()
  val listOfUser = User.users
  startServer(interface = "localhost", port = 8000){
    get{
       path("hello"){
         complete{
           "This is Hello Akka Example"
         }
       }
      path("world"){
        complete{
          "This is world Akka Example"
        }
      }
    } ~
    get{
      path("list" / "all"){
        respondWithMediaType(MediaType.custom("application/json")){
          complete{
            User.toJson(listOfUser)
          }
        }
      }
    }
  }
}
