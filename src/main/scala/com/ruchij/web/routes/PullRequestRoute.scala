package com.ruchij.web.routes

import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.server.Directives._
import com.ruchij.services.github.{GitHubService, PullRequestState}
import com.ruchij.utils.JsonFormatters._
import com.ruchij.web.directives.{Cors, ResponseHandler}
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
            ResponseHandler()
          }
        }
      } ~
      path("open") {
        get {
          onComplete(gitHubService.getPullRequests(gitRepoId, PullRequestState.Open)) {
            ResponseHandler()
          }
        }
      } ~
      pathPrefix(IntNumber) {
        pullReqNumber => {
          path("mergeable") {
            get {
              onComplete(gitHubService.isMergeable(gitRepoId, pullReqNumber)) {
                ResponseHandler(Mergeable)
              }
            }
          } ~
          path("merge") {
            post {
              entity(as[MergeRequest]) {
                mergeRequest =>
                  onComplete(gitHubService.mergePullRequest(gitRepoId, pullReqNumber, mergeRequest.message)) {
                    ResponseHandler()
                  }
              }
            } ~
            Cors.preflightRequest(HttpMethods.POST)
          }
        }
      }
    }

}
