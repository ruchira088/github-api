package com.ruchij.services.github

import org.eclipse.egit.github.core.{MergeStatus, PullRequest, Repository}

import scala.concurrent.Future

trait GitHubService
{
  def getRepositories(): Future[List[Repository]]

  def getPullRequests(repositoryId: String, state: PullRequestState): Future[List[PullRequest]]

  def mergePullRequest(repositoryId: String, pullRequestNumber: Int, message: String): Future[MergeStatus]
}
