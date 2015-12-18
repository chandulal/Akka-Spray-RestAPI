package com.demo.model

import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import org.json4s.ShortTypeHints

trait User{
  val name: String
}
case class NormalUser(name: String) extends User
case class PremiumUser(name: String, balance: Int) extends User

object User{

  val users = List[User](
    NormalUser("Bob"),
    NormalUser("Alice"),
    PremiumUser("Eve",100)
  )

  def getByName(name: String) = users.find(_.name==name)

  private implicit val format = Serialization.formats(ShortTypeHints(List(classOf[NormalUser],classOf[PremiumUser])))
  def toJson(users: List[User]): String = writePretty(users)
  def toJson(user: User) : String = writePretty(user)
}

