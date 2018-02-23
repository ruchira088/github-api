package com.ruchij.exceptions

case class MergeConflictException(gitRepo: String, pullRequestNumber: Int) extends Exception
{
  override def getMessage: String =
    s"Merge conflicts exist for merging PullRequest #$pullRequestNumber in $gitRepo"
}
