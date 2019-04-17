package org.morozov.webpush

import java.security.interfaces.{ECPrivateKey, ECPublicKey}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, Uri}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._


case class PushServiceAsync(publicKey: ECPublicKey, privateKey: ECPrivateKey, subject: String, exp: FiniteDuration = 12.hours)
                           (implicit system: ActorSystem) extends HttpPostRequest[Future, HttpResponse] with RequestBuilder {


  implicit val executionContext = system.dispatcher

  def send(subscription: Subscription, payload: Option[Array[Byte]], ttl: Int): Future[HttpResponse] = {

    val uri: String = subscription.endpoint

    val zero: Map[String, String] = vapidHeaders(subject, publicKey, privateKey, subscription.origin, ttl, exp)
    val (entity, rawHeaders) = payload.map { p ⇒
      val (encryptionHeaders, content) = handleEncryption(publicKey, p, subscription)
      val headers = zero ++ encryptionHeaders
      val rawHeaders = headers.map { case (k, v) ⇒ RawHeader(k, v) }
      val entity = HttpEntity(ContentTypes.`application/octet-stream`, content)
      (entity, rawHeaders)
    }.getOrElse {
      val rawHeaders = zero.map { case (k, v) ⇒ RawHeader(k, v) }
      val entity = HttpEntity.Empty
      (entity, rawHeaders)
    }

    val request = HttpRequest(
      uri = Uri(uri),
      method = HttpMethods.POST,
      entity = entity,
      headers = rawHeaders.toList
    )

    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    responseFuture.andThen { case _ ⇒
      println("the end")
    }
  }

}
