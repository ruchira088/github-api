package com.ruchij.web.directives

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.ruchij.web.responses.Error
import org.eclipse.egit.github.core.client.RequestException
import com.ruchij.utils.JsonFormatters._

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object ResponseHandler
{
  def apply[A, B](transform: A => B)(implicit toResponseMarshaller: ToResponseMarshaller[B]): PartialFunction[Try[A], Route] =
    {
      case Success(value) => complete(transform(value))

      case Failure(requestException: RequestException) =>
        complete(requestException.getStatus, Error(requestException.getError.getMessage))

      case Failure(NonFatal(exception)) =>
        complete(StatusCodes.InternalServerError, Error(exception.getMessage))

    }

  def apply[A]()(implicit toResponseMarshaller: ToResponseMarshaller[A]) =
    apply[A, A](identity)
}
