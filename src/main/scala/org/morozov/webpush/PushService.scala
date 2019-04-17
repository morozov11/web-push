package org.morozov.webpush

import scala.language.higherKinds


trait PushService[F[_], R] {

  protected val defaultTtl: Int = 2419200

  def send(subscription: Subscription, payload: String): F[R]

}
