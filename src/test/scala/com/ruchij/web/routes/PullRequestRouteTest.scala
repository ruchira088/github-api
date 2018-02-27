package com.ruchij.web.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ruchij.services.github.{GitHubService, PullRequestState}
import com.ruchij.utils.TestItemUtils._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class PullRequestRouteTest extends WordSpec with ScalatestRouteTest with Matchers with MockFactory
{
  "PullRequestRoute" should {

    val GIT_REPO = "gitRepo"
    val gitHubService = stub[GitHubService]

    (gitHubService.getPullRequests _).when(GIT_REPO, *)
      .onCall((_, state) => Future.successful(List(pullRequest(state))))

    "return a list of closed pull requests for GET requests to /pull-request/closed" in {

      Get("/pull-request/closed") ~> PullRequestRoute(GIT_REPO)(gitHubService) ~> check {

        status shouldEqual StatusCodes.OK

        contentType shouldEqual ContentTypes.`application/json`

        val closedPullRequest = pullRequest(PullRequestState.Closed)

        entityAs[String] shouldEqual removeNewLineChars {
          s"""[{
             |"number":${closedPullRequest.getNumber},
             |"state":"${closedPullRequest.getState}",
             |"id":${closedPullRequest.getId},
             |"createdAt":"${closedPullRequest.getCreatedAt}",
             |"title":"${closedPullRequest.getTitle}"
           |}]""".stripMargin
        }
      }
    }
  }

}
