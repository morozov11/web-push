package org.morozov.webpush

import java.security.interfaces.{ECPrivateKey, ECPublicKey}
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.morozov.webpush.implicits._
import scala.concurrent.duration._


/**
  * Push service.
  */
case class PushServiceSync(publicKey: ECPublicKey, privateKey: ECPrivateKey, subject: String, exp: FiniteDuration = 12.hours)
  extends PushService[Id, HttpResponse] with RequestBuilder {

  private val httpClient: HttpClient = HttpClients.createDefault


  protected def send(subscription: Subscription, payload: Option[Array[Byte]], ttl: Int) = {

    val httpPost = new HttpPost(subscription.endpoint)

    val zero = vapidHeaders(subject, publicKey, privateKey, subscription.origin, ttl, exp)
    payload.fold(zero) {
      p =>
        val (encryptionHeaders, content) = handleEncryption(publicKey, p, subscription)
        httpPost.setEntity(new ByteArrayEntity(content))
        zero ++ encryptionHeaders
    }.foreach { case (k, v) => httpPost.addHeader(new BasicHeader(k, v)) }
    httpClient.execute(httpPost)
  }

}
