package com.ruchij.services.github

import com.ruchij.ec.BlockingExecutionContext
import com.ruchij.exceptions.MergeConflictException
import org.eclipse.egit.github.core.service.{PullRequestService, RepositoryService}
import org.eclipse.egit.github.core.{MergeStatus, PullRequest, Repository}

import scala.collection.JavaConverters._
import scala.concurrent.Future

class GitHubServiceImpl(apiKey: String)(implicit blockingExecutionContext: BlockingExecutionContext) extends GitHubService
{
  private def authenticatedPullRequestService: PullRequestService =
    new PullRequestService() {
      getClient.setOAuth2Token(apiKey)
    }

  override def getRepositories(): Future[List[Repository]] =
    Future {
      val repositoryService = new RepositoryService() {
        getClient.setOAuth2Token(apiKey)
      }

      repositoryService.getRepositories.asScala.toList
    }

  override def getPullRequests(repositoryId: String, state: PullRequestState): Future[List[PullRequest]] =
    Future {
      authenticatedPullRequestService.getPullRequests(() => repositoryId, state.name).asScala.toList
    }

  override def mergePullRequest(repositoryId: String, pullRequestNumber: Int, message: String): Future[MergeStatus] =
    for {
      isMergeable <- isMergeable(repositoryId, pullRequestNumber)

      _ <- if (isMergeable) Future.successful((): Unit) else Future.failed(MergeConflictException(repositoryId, pullRequestNumber))

      mergeStatus = authenticatedPullRequestService.merge(() => repositoryId, pullRequestNumber, message)
    }
    yield mergeStatus

  override def isMergeable(repositoryId: String, pullRequestNumber: Int): Future[Boolean] =
    Future {
      authenticatedPullRequestService.getPullRequest(() => repositoryId, pullRequestNumber).isMergeable
    }
}

object GitHubServiceImpl
{
  def apply(apiKey: String)(implicit blockingExecutionContext: BlockingExecutionContext): GitHubServiceImpl =
    new GitHubServiceImpl(apiKey)
}
