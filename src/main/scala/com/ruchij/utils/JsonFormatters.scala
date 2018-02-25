package com.ruchij.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.ruchij.web.requests.MergeRequest
import com.ruchij.web.responses.Mergeable
import org.eclipse.egit.github.core.{MergeStatus, PullRequest, Repository}
import spray.json.{DefaultJsonProtocol, JsBoolean, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

object JsonFormatters extends DefaultJsonProtocol with SprayJsonSupport
{
  trait ResponseWriter[A] extends RootJsonFormat[A]
  {
    override def read(json: JsValue): A = throw new UnsupportedOperationException()
  }

  implicit def repositoryJsonFormat: ResponseWriter[Repository] =
    (repository: Repository) =>
      JsObject(
        "id" -> JsNumber(repository.getId),
        "fullId" -> JsString(repository.generateId()),
        "name" -> JsString(repository.getName),
        "pushedAt" -> JsString(repository.getPushedAt.toString),
        "language" -> JsString(Option(repository.getLanguage).getOrElse("UNKNOWN"))
      )

  implicit def pullRequestJsonFormat: ResponseWriter[PullRequest] =
    (pullRequest: PullRequest) =>
      JsObject(
        "id" -> JsNumber(pullRequest.getId),
        "number" -> JsNumber(pullRequest.getNumber),
        "title" -> JsString(pullRequest.getTitle),
        "createdAt" -> JsString(pullRequest.getCreatedAt.toString),
        "state" -> JsString(pullRequest.getState)
    )

  implicit def mergeStatusJsonFormat: ResponseWriter[MergeStatus] =
    (mergeStatus: MergeStatus) =>
      JsObject(
        "message" -> JsString(mergeStatus.getMessage),
        "sha" -> JsString(mergeStatus.getSha),
        "isMerged" -> JsBoolean(mergeStatus.isMerged)
      )

  implicit def mergeRequest: RootJsonFormat[MergeRequest] = jsonFormat1(MergeRequest.apply)

  implicit def mergeableResponse: RootJsonFormat[Mergeable] = jsonFormat1(Mergeable.apply)
}
