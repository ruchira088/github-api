package com.ruchij.utils

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  def convert[A, B](converter: A => B, value: A): Try[B] =
    try {
      Success(converter(value))
    } catch {
      case NonFatal(throwable) => Failure(throwable)
    }

  def parseInt(intStr: String) = convert[String, Int](_.toInt, intStr)
}
