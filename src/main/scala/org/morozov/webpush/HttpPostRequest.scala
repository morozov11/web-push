package org.morozov.webpush

trait HttpPostRequest[F[_], R] {

  def send(subscription: Subscription, payload: Option[Array[Byte]], ttl: Int): F[R]

}
