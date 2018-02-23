package com.ruchij.services.github

trait PullRequestState {
  def name: String
}

object PullRequestState
{
  case object Open extends PullRequestState {
    override def name: String = "open"
  }

  case object Merged extends PullRequestState {
    override def name: String = "merged"
  }

  case object Closed extends PullRequestState {
    override def name: String = "closed"
  }
}
