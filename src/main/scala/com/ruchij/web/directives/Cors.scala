package com.ruchij.web.directives

import akka.http.scaladsl.model.{HttpMethod, StatusCodes}
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.{Directive0, Route}
import akka.http.scaladsl.server.Directives._

object Cors
{
  def apply(): Directive0 =
    respondWithHeaders(`Access-Control-Allow-Origin`.*)

  def preflightRequest(httpMethods: HttpMethod*): Route =
    options {
      respondWithHeaders(
        `Access-Control-Allow-Methods`(httpMethods: _*),
        `Access-Control-Allow-Headers`("*")
      ) {
        complete(StatusCodes.OK)
      }
    }
}
