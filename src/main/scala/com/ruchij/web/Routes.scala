package com.ruchij.web

import akka.http.scaladsl.server.Directives._
import com.ruchij.services.github.{GitHubService, PullRequestState}
import com.ruchij.utils.JsonFormatters._

import scala.util.{Failure, Success}

object Routes
{
  def apply(gitHubService: GitHubService) =
    pathPrefix("repository") {
      pathEndOrSingleSlash {
        get {
          onComplete(gitHubService.getRepositories()) {
            case Success(repositories) => complete(repositories)
            case Failure(throwable) => complete(throwable.getMessage)
          }
        }
      } ~
      pathPrefix(Segment / Segment) {
        (username, gitRepo)  => {
          val repoId = s"$username/$gitRepo"

          pathPrefix("pull-requests") {
            path("closed") {
              get {
                onComplete(gitHubService.getPullRequests(repoId, PullRequestState.Closed)) {
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
      }
    }
}
