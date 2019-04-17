package org.morozov.webpush

import java.security.interfaces.{ECPrivateKey, ECPublicKey}
import java.util.Base64

import org.morozov.webpush.Encryption.Encrypted
import pdi.jwt.Jwt
import pdi.jwt.JwtAlgorithm.ES256

import scala.concurrent.duration.Duration

trait RequestBuilder {

  /**
    * Returns the server public key as a URL safe base64 string.
    */
  def publicKeyToBase64(publicKey: ECPublicKey): String = {
    val base64encoder: Base64.Encoder = Base64.getUrlEncoder
    base64encoder.withoutPadding().encodeToString(Utils.publicKeyToBytes(publicKey))
  }

  protected def handleEncryption(publicKey: ECPublicKey, payload: Array[Byte], subscription: Subscription): (Map[String, String], Array[Byte]) = {
    val base64encoder: Base64.Encoder = Base64.getUrlEncoder
    val encrypted: Encrypted = Encryption.encrypt(payload, subscription.publicKey, subscription.auth)
    (Map(
      "Content-Encoding" -> "aesgcm",
      "Encryption" -> ("keyid=p256dh;salt=" + base64encoder.withoutPadding().encodeToString(encrypted.salt)),
      "Crypto-Key" -> ("keyid=p256dh;dh=" + base64encoder.encodeToString(Utils.publicKeyToBytes(encrypted.publicKey.asInstanceOf[ECPublicKey])) +
        ";p256ecdsa=" + base64encoder.withoutPadding().encodeToString(Utils.publicKeyToBytes(publicKey)))
    ), encrypted.ciphertext)
  }

  protected def vapidHeaders(subject: String,
                             publicKey: ECPublicKey,
                             privateKey: ECPrivateKey,
                             origin: String,
                             ttl: Int,
                             exp: Duration): Map[String, String] = {
    Map(
      "TTL" -> ttl.toString,
      "Authorization" -> (
        "WebPush " + Jwt.encode(Utils.toJsonString(Map(
          "aud" -> origin,
          "exp" -> ((System.currentTimeMillis() + exp.toMillis) / 1000).toString,
          "sub" -> subject
        )), privateKey, ES256)),
      "Crypto-Key" -> ("p256ecdsa=" + publicKeyToBase64(publicKey))
    )
  }

}
