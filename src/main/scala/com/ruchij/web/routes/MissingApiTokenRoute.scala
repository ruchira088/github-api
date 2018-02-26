package com.ruchij.web.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import com.ruchij.exceptions.GitHubApiTokenUndefinedException
import com.ruchij.web.responses.Error
import com.ruchij.web.directives.Cors
import com.ruchij.utils.JsonFormatters._

object MissingApiTokenRoute
{
  def apply() =
    Cors() {
      complete(
        StatusCodes.BadGateway,
        Error(GitHubApiTokenUndefinedException.getMessage)
      )
    }
}
