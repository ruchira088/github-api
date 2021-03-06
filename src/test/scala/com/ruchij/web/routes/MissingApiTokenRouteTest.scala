package com.ruchij.web.routes

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ruchij.exceptions.GitHubApiTokenUndefinedException
import com.ruchij.utils.TestItemUtils._
import org.scalatest.{FlatSpec, Matchers}

class MissingApiTokenRouteTest extends FlatSpec with ScalatestRouteTest with Matchers
{
  "MissingApiTokenRoute" should
    "return 502 status code with missing API token error message for any requests" in {

    Get("/") ~> MissingApiTokenRoute() ~> check {

      status shouldEqual StatusCodes.BadGateway

      contentType shouldEqual ContentTypes.`application/json`

      entityAs[String] shouldEqual removeNewLineChars {
        s"""{
          |"errorMessage":"${GitHubApiTokenUndefinedException.getMessage}"
          |}""".stripMargin
      }
    }
  }
}
