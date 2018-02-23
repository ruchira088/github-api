package com.ruchij.ec

import akka.actor.ActorSystem
import akka.dispatch.MessageDispatcher

import scala.concurrent.ExecutionContext

class BlockingExecutionContext()(implicit actorSystem: ActorSystem) extends ExecutionContext
{
  val messageDispatcher: MessageDispatcher = actorSystem.dispatchers.lookup("blocking-execution-context")

  override def execute(runnable: Runnable): Unit = messageDispatcher.execute(runnable)

  override def reportFailure(cause: Throwable): Unit = messageDispatcher.reportFailure(cause)
}

object BlockingExecutionContext
{
  def apply()(implicit actorSystem: ActorSystem): BlockingExecutionContext =
    new BlockingExecutionContext()
}