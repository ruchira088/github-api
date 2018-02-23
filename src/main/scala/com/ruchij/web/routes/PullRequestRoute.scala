package com.ruchij.web.routes

import akka.http.scaladsl.server.Directives._
import com.ruchij.services.github.{GitHubService, PullRequestState}
import com.ruchij.utils.JsonFormatters._
import com.ruchij.web.requests.MergeRequest

import scala.util.Success

object PullRequestRoute
{
  def apply(gitRepoId: String)(gitHubService: GitHubService) =
    pathPrefix("pull-requests") {
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
      path(IntNumber / "merge") {
        pullReqNumber => {
          post {
            entity(as[MergeRequest]) {
              mergeRequest =>
                onComplete(gitHubService.mergePullRequest(gitRepoId, pullReqNumber, mergeRequest.message)) {
                  case Success(_) => complete("")
                }
            }
          }
        }
      }
    }

}
