package org.morozov.webpush
import scala.language.higherKinds


class PushService[F[_], R]() {

  protected val defaultTtl: Int = 2419200

  /**
    * Send a data free push notification.
    *
    * @param subscription Browser subscription object.
    * @return HttpResponse from push server.
    */
  def send(subscription: Subscription)(implicit ev: HttpPostRequest[F,R]): F[R] =
    send(subscription, None, defaultTtl)

  /**
    * Send a data free push notification.
    *
    * @param subscription Browser subscription object.
    * @param ttl          Suggestion to the message server for how long it should keep the message
    *                     and attempt to deliver it.
    * @return HttpResponse from push server.
    */
  def send(subscription: Subscription, ttl: Int)(implicit ev: HttpPostRequest[F,R]): F[R] =
    send(subscription, None, ttl)

  /**
    * Sends a data bearing push notification.
    *
    * @param subscription Browser subscription object.
    * @param payload      Push notification payload.
    * @param ttl          Optional suggestion to the message server for how long it should keep the message
    *                     and attempt to deliver it. If not specified default value will be used.
    * @return HttpResponse from push server.
    */
  def send(subscription: Subscription, payload: String, ttl: Int)(implicit ev: HttpPostRequest[F,R]): F[R] =
    send(subscription, Some(payload.getBytes), ttl)

  def send(subscription: Subscription, payload: String)(implicit ev: HttpPostRequest[F,R]): F[R] =
    send(subscription, Some(payload.getBytes), defaultTtl)

  /**
    *
    * Sends a data bearing push notification.
    *
    * @param subscription Browser subscription object.
    * @param payload      Push notification data as a Byte Array.
    * @param ttl          Optional suggestion to the message server for how long it should keep the message
    *                     and attempt to deliver it. If not specified default value will be used.
    * @return HttpResponse from push server.
    */
  def send(subscription: Subscription, payload: Array[Byte], ttl: Int = defaultTtl)(implicit ev: HttpPostRequest[F,R]): F[R] =
    send(subscription, Some(payload), ttl)

  protected def send(subscription: Subscription, payload: Option[Array[Byte]], ttl: Int)(implicit ev: HttpPostRequest[F,R]): F[R] = {
    ev.send(subscription, payload, ttl)
  }


}
