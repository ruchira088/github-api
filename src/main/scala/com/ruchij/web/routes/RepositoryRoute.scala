package com.ruchij.web.routes

import akka.http.scaladsl.server.Directives._
import com.ruchij.services.github.GitHubService
import com.ruchij.utils.JsonFormatters._

import scala.util.{Failure, Success}

object RepositoryRoute
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
        (username, gitRepo) =>
          PullRequestRoute(s"$username/$gitRepo")(gitHubService)
      }
    }

}
