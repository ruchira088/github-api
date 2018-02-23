package com.ruchij.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.eclipse.egit.github.core.{PullRequest, Repository}
import spray.json.{DefaultJsonProtocol, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

object JsonFormatters extends DefaultJsonProtocol with SprayJsonSupport
{
  trait ResponseWriter[A] extends RootJsonFormat[A]
  {
    override def read(json: JsValue): A = throw new UnsupportedOperationException()
  }

  implicit def repositoryJsonFormat: RootJsonFormat[Repository] = new RootJsonFormat[Repository]
  {
    override def write(repository: Repository): JsValue = JsObject(
      "id" -> JsNumber(repository.getId),
      "fullId" -> JsString(repository.generateId()),
      "name" -> JsString(repository.getName),
      "language" -> JsString(Option(repository.getLanguage).getOrElse("UNKNOWN"))
    )

    override def read(json: JsValue): Repository = throw new UnsupportedOperationException()
  }

  implicit def pullRequestJsonFormat: RootJsonFormat[PullRequest] = new RootJsonFormat[PullRequest]
  {
    override def write(pullRequest: PullRequest): JsValue = JsObject(
      "id" -> JsNumber(pullRequest.getId),
      "number" -> JsNumber(pullRequest.getNumber),
      "title" -> JsString(pullRequest.getTitle),
      "createdAt" -> JsString(pullRequest.getCreatedAt.toString),
      "state" -> JsString(pullRequest.getState)
    )

    override def read(json: JsValue): PullRequest = throw new UnsupportedOperationException()
  }
}
