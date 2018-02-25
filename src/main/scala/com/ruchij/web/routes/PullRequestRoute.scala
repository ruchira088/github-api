package com.ruchij.web.routes

import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.server.Directives._
import com.ruchij.services.github.{GitHubService, PullRequestState}
import com.ruchij.utils.JsonFormatters._
import com.ruchij.web.directives.Cors
import com.ruchij.web.requests.MergeRequest
import com.ruchij.web.responses.Mergeable

import scala.util.Success

object PullRequestRoute
{
  def apply(gitRepoId: String)(gitHubService: GitHubService) =
    pathPrefix("pull-request") {
      path("closed") {
        get {
          onComplete(gitHubService.getPullRequests(gitRepoId, PullRequestState.Closed)) {
            case Success(pullRequests) => complete(pullRequests)
          }
        }
      } ~
      path("open") {
        get {
          onComplete(gitHubService.getPullRequests(gitRepoId, PullRequestState.Open)) {
            case Success(pullRequests) => complete(pullRequests)
          }
        }
      } ~
      pathPrefix(IntNumber) {
        pullReqNumber => {
          path("mergeable") {
            get {
              onComplete(gitHubService.isMergeable(gitRepoId, pullReqNumber)) {
                case Success(isMergeable) =>
                  complete(Mergeable(isMergeable))
              }
            }
          } ~
          path("merge") {
            post {
              entity(as[MergeRequest]) {
                mergeRequest =>
                  onComplete(gitHubService.mergePullRequest(gitRepoId, pullReqNumber, mergeRequest.message)) {
                    case Success(mergeStatus) => complete(mergeStatus)
                  }
              }
            } ~
            Cors.preflightRequest(HttpMethods.POST)
          }
        }
      }
    }

}
