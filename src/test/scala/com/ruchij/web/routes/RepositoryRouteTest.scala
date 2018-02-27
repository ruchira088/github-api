package com.ruchij.web.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ruchij.services.github.GitHubService
import com.ruchij.utils.TestItemUtils._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future

class RepositoryRouteTest extends FlatSpec with ScalatestRouteTest with Matchers with MockFactory
{
  "RepositoryRoute" should
    "return a list of repositories for GET requests to /repository" in {

      val gitHubService = stub[GitHubService]
      (gitHubService.getRepositories _).when().returns(Future.successful(List(repository)))

      Get("/repository") ~> RepositoryRoute(gitHubService) ~> check {

        status shouldEqual StatusCodes.OK

        contentType shouldEqual ContentTypes.`application/json`

        entityAs[String] shouldEqual removeNewLineChars {
          s"""[{
             |"name":"${repository.getName}",
             |"fullId":"${repository.generateId()}",
             |"id":${repository.getId},
             |"language":"${repository.getLanguage}",
             |"pushedAt":"${repository.getPushedAt.toString}"
             |}]""".stripMargin
        }
      }
    }
}
