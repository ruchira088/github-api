package com.ruchij.web.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ruchij.services.github.{GitHubService, PullRequestState}
import com.ruchij.utils.TestItemUtils._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Assertion, FlatSpec, Matchers}

import scala.concurrent.Future

class PullRequestRouteTest extends FlatSpec with ScalatestRouteTest with Matchers with MockFactory
{
  def pullRequestAssertion(pullRequestState: PullRequestState): Assertion =
  {
    status shouldEqual StatusCodes.OK

    contentType shouldEqual ContentTypes.`application/json`

    val pullRequestValue = pullRequest(pullRequestState)

    entityAs[String] shouldEqual removeNewLineChars {
      s"""[{
         |"number":${pullRequestValue.getNumber},
         |"state":"${pullRequestValue.getState}",
         |"id":${pullRequestValue.getId},
         |"createdAt":"${pullRequestValue.getCreatedAt}",
         |"title":"${pullRequestValue.getTitle}"
         |}]""".stripMargin
    }
  }

  def pullRequestRoute() = {
    val GIT_REPO = "gitRepo"
    val gitHubService = stub[GitHubService]

    (gitHubService.getPullRequests _)
      .when(GIT_REPO, *)
      .onCall((_, state) => Future.successful(List(pullRequest(state))))

    PullRequestRoute(GIT_REPO)(gitHubService)
  }

  "PullRequestRoute" should
    "return a list of closed pull requests for GET requests to /pull-request/closed" in {

      Get("/pull-request/closed") ~> pullRequestRoute() ~> check {
          pullRequestAssertion(PullRequestState.Closed)
        }
    }

  it should
    "return a list of open pull requests for GET requests to /pull-request/open" in {

    Get("/pull-request/open") ~> pullRequestRoute() ~> check {
      pullRequestAssertion(PullRequestState.Open)
    }
  }
}
