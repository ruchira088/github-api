package com.ruchij.web.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.ruchij.services.github.GitHubService

import scala.concurrent.ExecutionContext

object IndexRoute
{
  def apply(gitHubService: GitHubService)(implicit executionContext: ExecutionContext) =
    path("") {
      get {
        complete(
          StatusCodes.OK,
          HttpEntity(ContentTypes.`application/json`, """{"serviceName": "GitHub API"}""")
        )
      }
    } ~
    RepositoryRoute(gitHubService)

}
