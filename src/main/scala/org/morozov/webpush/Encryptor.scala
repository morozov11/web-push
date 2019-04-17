package org.morozov.webpush

import java.security.interfaces.ECPublicKey
import java.util.Base64

import org.morozov.webpush.Encryption.Encrypted

trait Encryptor {

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

}
