package com.ruchij.web.routes

import akka.http.scaladsl.server.Directives._
import com.ruchij.services.github.GitHubService
import com.ruchij.utils.JsonFormatters._
import com.ruchij.web.directives.ResponseHandler

object RepositoryRoute
{
  def apply(gitHubService: GitHubService) =
    pathPrefix("repository") {
      pathEndOrSingleSlash {
        get {
          onComplete(gitHubService.getRepositories()) {
            ResponseHandler()
          }
        }
      } ~
      pathPrefix(Segment / Segment) {
        (username, gitRepo) =>
          PullRequestRoute(s"$username/$gitRepo")(gitHubService)
      }
    }

}
