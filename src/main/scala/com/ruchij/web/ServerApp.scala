package com.ruchij.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.ruchij.constants.{DefaultConfig, EnvVariableNames}
import com.ruchij.ec.BlockingExecutionContext
import com.ruchij.exceptions.GitHubApiTokenUndefinedException
import com.ruchij.services.github.GitHubServiceImpl
import com.ruchij.utils.ScalaUtils
import com.ruchij.web.routes.IndexRoute

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

object ServerApp
{
  def main(args: Array[String]): Unit =
  {
    implicit val actorSystem: ActorSystem = ActorSystem("github-api")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher
    val blockingExecutionContext = BlockingExecutionContext()

    val serverBinding =
      for {
        gitHubApiKey <- Future.fromTry {
          sys.env.get(EnvVariableNames.GITHUB_API_KEY)
            .fold[Try[String]](Failure(GitHubApiTokenUndefinedException))(Success(_))
        }

        gitHubService = GitHubServiceImpl(gitHubApiKey)(blockingExecutionContext)

        server <- Http().bindAndHandle(IndexRoute(gitHubService), "0.0.0.0", httpPort)
      }
      yield server

    serverBinding.onComplete {

      case Success(_) => println(s"Server is listening on port $httpPort...")

      case Failure(throwable) => {
        System.err.println(throwable.getMessage)
        System.exit(1)
      }
    }

    Await.ready(Promise[Unit].future, Duration.Inf)
  }

  def httpPort(): Int =
    sys.env.get(EnvVariableNames.HTTP_PORT)
      .fold(DefaultConfig.HTTP_PORT)(ScalaUtils.parseInt(_).getOrElse(DefaultConfig.HTTP_PORT))
}
