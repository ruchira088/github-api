package com.ruchij.utils

import java.util.Date

import com.ruchij.services.github.PullRequestState
import org.eclipse.egit.github.core.{PullRequest, Repository}
import org.scalamock.scalatest.MockFactory

object TestItemUtils extends MockFactory
{
  val date = new Date()

  def repository: Repository = {
    val repository = stub[Repository]

    (repository.generateId _).when().returns("generatedId")
    (repository.getId _).when().returns(123L)
    (repository.getName _).when().returns("sample-repo")
    (repository.getLanguage _).when().returns("Scala")
    (repository.getPushedAt _).when().returns(date)

    repository
  }

  def pullRequest(state: PullRequestState) = {
    val pullRequest = stub[PullRequest]

    (pullRequest.getId _).when().returns(456L)
    (pullRequest.getNumber _).when().returns(7)
    (pullRequest.getTitle _).when().returns("Pull Request Title")
    (pullRequest.getCreatedAt _).when().returns(date)
    (pullRequest.getState _).when().returns(state.name)

    pullRequest
  }

  def removeNewLineChars(string: String): String = string.replaceAll("\n", "")

}
