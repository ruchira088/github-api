package com.ruchij.web.directives

import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._

object Cors
{
  def apply(): Directive0 =
    respondWithHeaders(`Access-Control-Allow-Origin`.*)
}
