package com.ruchij.services.github

import org.eclipse.egit.github.core.{MergeStatus, PullRequest, Repository}
import org.eclipse.egit.github.core.service.{PullRequestService, RepositoryService}

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

class GitHubServiceImpl(apiKey: String)(implicit executionContext: ExecutionContext) extends GitHubService
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

  override def mergePullRequest(repositoryId: String, pullRequestId: Int, message: String): Future[MergeStatus] =
    Future {
      authenticatedPullRequestService.merge(() => repositoryId, pullRequestId, message)
    }

}

object GitHubServiceImpl
{
  def apply(apiKey: String)(implicit executionContext: ExecutionContext): GitHubServiceImpl =
    new GitHubServiceImpl(apiKey)
}
