package com.ruchij.exceptions

object GitHubApiTokenUndefinedException extends Exception
{
  override def getMessage: String = "GITHUB_API_KEY is NOT defined as an ENV value."
}
