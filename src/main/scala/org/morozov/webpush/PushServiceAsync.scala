package org.morozov.webpush

import java.security.interfaces.{ECPrivateKey, ECPublicKey}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._


case class PushServiceAsync(publicKey: ECPublicKey, privateKey: ECPrivateKey, subject: String, exp: FiniteDuration = 12.hours)
                           (implicit system: ActorSystem) extends HttpPostRequest[Future, HttpResponse] {


  implicit val executionContext = system.dispatcher

  def send(subscription: Subscription, payload: Option[Array[Byte]], ttl: Int): Future[HttpResponse] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "https://akka.io"))
    responseFuture
  }

}
