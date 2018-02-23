package com.ruchij.web.routes

import akka.http.scaladsl.server.Directives._
import com.ruchij.services.github.{GitHubService, PullRequestState}
import com.ruchij.utils.JsonFormatters._

import scala.concurrent.ExecutionContext
import scala.util.Success

object PullRequestRoute
{
  def apply(gitRepoId: String)(gitHubService: GitHubService)(implicit executionContext: ExecutionContext) =
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
          complete("open")
        }
      }
    }

}
